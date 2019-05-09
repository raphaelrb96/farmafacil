const functions = require('firebase-functions');
const admin = require('firebase-admin');
const app = admin.initializeApp();

const db = admin.firestore();
const settings = {/* your settings... */ timestampsInSnapshots: true};
db.settings(settings);

const acaoCompra = 'compra';
const acaoMensagem = 'mensagem';

const produtoPesquisa = 1;

const collectionUsuario = db.collection('Usuario');
const collectionUsuarioAnalytics = db.collection('UsuarioAnalytics');
const collectionProdutosAnalytics = db.collection('ProdutosAnalytics');
const collectionCarrinhoAnalytics = db.collection('CarrinhoAnalytics');
const searchTermosAnalytics = db.collection('termosDePesquisa');
const searchUserAnalytics = db.collection('termosDePesquisaUser');


//ACIONADORES ANALYTICS

exports.novoLoginFace = functions.analytics.event('LoginFace').onLog(e => {
	let provedor = 'Facebook';
	let event = e.params;
	return atualizarUsuario(event.UidUsuario, event.NomeUsuario, provedor);
});

exports.novoLoginGoogle = functions.analytics.event('LoginGoogle').onLog(e => {
	let provedor = 'Google';
	let event = e.params;
	return atualizarUsuario(event.UidUsuario, event.NomeUsuario, provedor);
});

exports.novaPesquisa = functions.analytics.event('PesquisaProduto').onLog(e => {
	let event = e.params;
	const nomePesquisa = event.NomePesquisa;
	const uidUserSearch = event.UidUsuario;
	const nomeUsuario = event.NomeUsuario;

	return analizarTermoDePesquisa(nomePesquisa, uidUserSearch, nomeUsuario);
	
});

exports.produtoAdicionadoAoCart = functions.analytics.event('AdicionarAoCarrinho').onLog(e => {
	let event = e.params;
	return produtoAdicionado(event.IdProduto);
});

exports.usuarioAdicionaProdutoAoCart = functions.analytics.event('UsuarioAdicionaProdutoAoCart').onLog(e => {
	let event = e.params;
	return userAdicionaAoCarrinho(event.IdProduto, event.UidUsuario, event.Nome);
});

//ACIONADORES FIRESTORE

exports.atualizarTimestampMensagem = functions.firestore.document('mensagens/ativas/{uidUser}/{idDocument}').onCreate((snap, context) => {
    const newDoc = snap.data();
    return snap.ref.update({
        timeStamp : Date.now()
    });
});

exports.atualizarTimestampCentralMensagem = functions.firestore.document('centralMensagens/{uidUser}').onWrite((change, context) => {
    const newDoc = change.after.data();
    return snap.ref.update({
        timeNovaMensagem : Date.now()
    });
});

exports.atualizarTimestampNovoUsuario = functions.firestore.document('Usuario/{uid}').onCreate((snap, context) => {
    const newDoc = snap.data();
    const dateNow = Date.now();
    return snap.ref.update({
        ultimoLogin: dateNow,
        primeiroLogin: dateNow
    });
});

exports.enviarNotificacaoMensagem = functions.firestore.document('mensagens/ativas/{uidUser}/{idDocument}').onUpdate((change, context) => {
    const newDoc = change.after.data();
    const uidUser = context.params.uidUser;
    const userId = newDoc.uidUser;

    let body;

    if (String(newDoc.menssagemText).length < 2) {
    	body = 'Você recebeu uma nova mensagem!';
    } else {
    	body = newDoc.menssagemText;
    }

    if(uidUser !== userId) {
    	let notifUser = enviarNotificacaoUsuario(acaoMensagem, body, 'Nova Mensagem');
        return notifUser;
    } else {
        let notificacao = enviarNotificacaoAdmin(acaoMensagem, body, 'Nova Mensagem');
    	return notificacao;
    }
});

exports.novaCompraRealizada = functions.firestore.document('centralComprasPendentes/{idDoc}').onUpdate((change, context) => {
	const newValue = change.after.data();
	const valorTT = String(newValue.valorTotal).charAt(1);
	let body = newValue.userNome + ' efetuou um compra de R$ ' + valorTT + ',00';
    let notificacao = enviarNotificacaoAdmin(acaoCompra, 'Nova Compra', body);
    return notificacao;
});

exports.atualizarTimestampCompraRealizada = functions.firestore.document('centralComprasPendentes/{idDoc}').onCreate((snap, context) => {
    const newDoc = snap.data();
    return snap.ref.update({
        hora : Date.now()
    });
});


//FUNCOES

let userAdicionaAoCarrinho = (id, uid, nomeProd) => {

	let time = Date.now();

	let itemCart = {
		numeroAddCart: 1,
		ultimaVezAdicionadoAoCart: time,
		nome: nomeProd
	};

	const refProdCarrinhoAnalyics = collectionCarrinhoAnalytics.doc(uid).collection('produtos').doc(id);
	refProdCarrinhoAnalyics.get().then(doc => {
		if (doc.exists) {
			itemCart.numeroAddCart = doc.data().numeroAddCart + 1;
		}

		return refProdCarrinhoAnalyics.set(itemCart);

	}).catch(error => {
		console.log(error);
		return 0;
	})
}


let produtoAdicionado = (id) => {

	let batch = db.batch();

	let time = Date.now();

	const obj = {
		numeroAddCart: 1,
		ultimaVezAdicionadoAoCart: time
	};

	const refProdAnalyticsAtual = collectionProdutosAnalytics.doc(id);
	refProdAnalyticsAtual.get().then(doc => {
		if (doc.exists) {
			obj.numeroAddCart = doc.data().numeroAddCart + 1;
			batch.update(refProdAnalyticsAtual, obj);
		} else {
			batch.set(refProdAnalyticsAtual, obj);
		}

		return batch.commit().then(() => {
			console.log('Sucesso ao adicionar Produto ao carrinho');
		}).catch(error => {
			console.log('Erro ao adicionar Produto ao carrinho');
			console.log(error);
		});

	}).catch(error => {
		console.log(error);
	});
};

let analizarTermoDePesquisa = (termo, uid, user) => {

	let time = Date.now();

	let batchSearch = db.batch();

	const obj = {
		termo: termo,
		quantidade: 1,
		ultimaPesquisa: time
	};

	const refTermoPesquisa = searchTermosAnalytics.doc(termo);
	const refUserTermoPesquisa = searchUserAnalytics.doc(uid).collection('termos').doc();

	refTermoPesquisa.get().then(doc => {

		batchSearch.set(refUserTermoPesquisa, obj);

		if (doc.exists) {
			obj.quantidade = doc.data().quantidade + 1;
		} else {
			obj.quantidade = 1;
		}

		batchSearch.set(refTermoPesquisa, obj);

		return batchSearch.commit().then(() => {
			console.log(user + ' efetuou uma nova pesquisa foi cadastrada');
		}).catch(error => {
			console.log('Nova pesquisa NAO foi cadastrada');
			console.log(error);
		});
		

	});
};


let enviarNotificacaoUsuario = (action, menssagem, titulo) => {
	collectionUsuario.doc(uidUser).get()
                .then(doc => {
                    if(!doc.exists) {
                        console.log('Voce procurou o token de um usuario, que ainda nao existe');
                        return null;
                    } else {
                        const tokenUser = doc.data().tokenFcm;
                        return tokenUser;
                    }
                }).then(tokenUser => {
                	if (tokenUser === null) {
                		return 0;
                	}
                    return notificacaoIndividual(action, menssagem, titulo, token);
                }).catch((error) => {
                    console.log('Erro Token usuario', error);
                    return 0;
                });
}

let enviarNotificacaoAdmin = (action, menssagem, titulo) => {
	let array = [];
	db.collection('Adm').get().then(snapshot => {
		snapshot.forEach((doc, i) => {
			array[i] = doc.data().tokenFcm;
		});

		return array;
	}).then(array => {
		if (array.length === 0) {
			console.log('Nenhum Admin cadastrado');
			return 0;
		}
		
		return notificacaoEmGrupo(action, menssagem, titulo, array);

	}).catch(error => {
		console.log('Erro Token Admin', error);
        return 0;
	})
};

let notificacaoIndividual = (action, menssagem, titulo, token) => {
	const menssagemNotficacao = {
            android: {
                ttl: 3600 * 1000,
                priority: 'normal',
                notification: {
                    title: titulo,
                    body: menssagem,
                    clickAction: action
                }
            },
            token: token
        };

    return admin.messaging().send(menssagemNotficacao).then(string => {
    	console.log(response.successCount + 'Notificacão enviada com sucesso');
    }).catch(error => {
    	console.log(error);
    });
};

let notificacaoEmGrupo = (action, menssagem, titulo, array) => {
	const menssagemNotficacao = {
            android: {
                ttl: 3600 * 1000,
                priority: 'normal',
                notification: {
                    title: titulo,
                    body: menssagem,
                    clickAction: action
                }
            },
            tokens: array
        };

    return admin.messaging().sendMulticast(menssagemNotficacao).then(response => {
    	console.log(response.successCount + ' notificacoes foram enviadas simutaneamente');
    }).catch(error => {
    	console.log(error);
    });
};

let atualizarUsuario = (uid, nome, provedor) => {

	let time = Date.now();

	let timeNovoLogin = {
	    ultimoLogin: time
	};

	const refDocUser = collectionUsuario.doc(uid);

	refDocUser.get().then(doc => {


		if (doc.exists) {
			//usuario ja existe
			console.log(nome + ' fez login pelo ' + provedor);

			refDocUser.update(timeNovoLogin).then(() => {
				console.log('O Horario do ultimo login de ' + nome + ' foi atualizado');
				return 1;
			}).catch(erro => {
				console.log('O Horario do ultimo login de ' + nome + ' não foi atualizado');
				console.log(erro);
				return 0;
			});
			
		} else {
			//usuario novo
			console.log(nome + ' fez login pela primeira vez usando ' + provedor);
			return 1;
		}


	}).catch(erro => {
		console.log(erro);
		return 0;
	});

};