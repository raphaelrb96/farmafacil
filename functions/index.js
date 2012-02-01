const functions = require('firebase-functions');
const admin = require('firebase-admin');
const app = admin.initializeApp({
   credential: admin.credential.applicationDefault(),
   databaseURL: "https://remediorapido-203dd.firebaseio.com"
});

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
const enderecosClientes = db.collection('Enderecos').doc('Clientes');


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

exports.viewCheckout = functions.analytics.event('UserVisitaCheckout').onLog(e => {
	let event = e.params;
	const idUser = event.IdUser;
});

exports.viewCart = functions.analytics.event('UserVisitaCarrinho').onLog(e => {
	let event = e.params;
	const idUser = event.IdUser;
	const nomeUsuario = event.NomeUser;

	userVizualizaCarrinho(idUser);
	return 0;
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

exports.checkNovoEndereco = functions.firestore.document('Enderecos/Clientes/{uidUser}/{idDocument}').onCreate((snap, context) => {
	const uidUser = context.params.uidUser;
	const idDocument = context.params.idDocument;
	const newDoc = snap.data();

	const enderecoRef = enderecosClientes.collection(uidUser);

	enderecoRef.get().then(snapshot => {

		//let batch = db.batch();

		let time = Date.now();

		let objeto = {
			numSelected: 1,
			timeUltimoSelected: time
		};

		if (snapshot.size > 0) {


			snapshot.forEach((doc, i) => {

				let item = doc.data();

				if ((item.latitude === newDoc.latitude && item.longitude === newDoc.longitude) || item.enderecoCompleto === newDoc.enderecoCompleto || item.adress === newDoc.adress) {

					objeto.numSelected = item.numSelected + 1;
					return enderecoRef.doc(item.idEndereco).update(objeto);

				} 

			});

			return adicionarNovoEndereco(newDoc, enderecoRef.doc());

		} else {
			return adicionarNovoEndereco(newDoc, enderecoRef.doc());
		}

	});

});

exports.atualizarTimestampMensagem = functions.firestore.document('mensagens/ativas/{uidUser}/{idDocument}').onCreate((snap, context) => {
    const newDoc = snap.data();
    let batch = db.batch();
    batch.update(snap.ref, {
        timeStamp: Date.now()
    });
    let finalRef = db.collection('centralMensagens').doc(context.params.uidUser);
    batch.update(finalRef, {
        timeNovaMensagem : Date.now()
    });
    return batch.commit();
});

//exports.atualizarTimestampCentralMensagens = functions.firestore.document('centralMensagens/{uidUser}').onWrite((change, context) => {
//    const newDoc = change.after.data();
//    if (newDoc.timeNovaMensagem === change.before.data().timeNovaMensagem) return null;
//    return change.after.ref.set({
//        timeNovaMensagem : Date.now()
//    },{merge:true});
//});

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
    	return enviarNotificacaoUsuario(acaoMensagem, body, 'Nova Mensagem', uidUser);
    } else {
        return enviarNotificacaoAdmin(acaoMensagem, body, 'Nova Mensagem');
    }
});

exports.novaCompraRealizada = functions.firestore.document('centralComprasPendentes/{idDoc}').onUpdate((change, context) => {
	const newValue = change.after.data();
	const valorTT = String(newValue.valorTotal).charAt(1);
	let body = newValue.userNome + ' efetuou um compra de R$ ' + valorTT + ',00';
    return enviarNotificacaoAdmin(acaoCompra, 'Nova Compra', body);
});

exports.atualizarTimestampCompraRealizada = functions.firestore.document('centralComprasPendentes/{idDoc}').onCreate((snap, context) => {
    const newDoc = snap.data();
    return snap.ref.update({
        hora : Date.now()
    });
});


//FUNCOES

let adicionarNovoEndereco = (objEndereco, ref) => {
	return ref.set(objEndereco);
};

let userVisitaCheckout = id => {
	const reff = collectionUsuarioAnalytics.doc(id);
	const time = Date.now();

	let obj = {
		numeroDeViewsCheckout: 1,
		ultimoCheckOut: time
	};

	reff.get().then(doc => {
		if (doc.exists) {
			let dados = doc.data();
			obj.numeroDeViewsCheckout = dados.numeroDeViewsCheckout + 1;
			return reff.update(obj);
		} else {
			return reff.set(obj);
		}
	}).catch(error => {
		console.log(error);
		return 0;
	});

};

let userVizualizaCarrinho = id => {
	const reff = collectionUsuarioAnalytics.doc(id);
	const time = Date.now();
	let obj = {
		ultimaViewCart: time,
		numeroDeViewsCarrinho: 1
	};

	reff.get().then(doc => {
		if (doc.exists) {
			let dados = doc.data();
			obj.numeroDeViewsCarrinho = dados.numeroDeViewsCarrinho + 1;
			return reff.update(obj);
		} else {
			return reff.set(obj);
		}
	}).catch(error => {
		console.log(error);
		return 0;
	});
};

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
};


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


let enviarNotificacaoUsuario = (action, menssagem, titulo, uidUser) => {
	collectionUsuario.doc(uidUser).get()
                .then(doc => {
                    if(!doc.exists) {
                        console.log('Voce procurou o token de um usuario, que ainda nao existe');
                        return null;
                    } else {
                        const tokenUser = doc.data().tokenFcm;
                        if (tokenUser === null) {
                            return 0;
                        }
                        return notificacaoIndividual(action, menssagem, titulo, token);
                    }
                }).catch((error) => {
                    console.log('Erro Token usuario', error);
                    return 0;
                });
}

let enviarNotificacaoAdmin = (action, menssagem, titulo) => {

	db.collection('Adm').get().then(snapshot => {

		let array = new Array();

		snapshot.forEach(doc => {
		    if(!array.includes(doc.data().tokenFcm)) {
		    	array.push(doc.data().tokenFcm);
		    }

		});

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

                    data: {
                        title: titulo,
                        body: menssagem,
                        clickAction: action
                    }

    };

    return admin.messaging().sendToDevice(token, menssagemNotficacao).then(string => {
    	console.log(response.successCount + 'Notificacão enviada com sucesso');
    }).catch(error => {
    	console.log(error);
    });
};

let notificacaoEmGrupo = (action, menssagem, titulo, array) => {
	let menssagemNotficacao = {
                    data: {
                        title: titulo,
                        body: menssagem,
                        clickAction: action
                    }
    };

    return admin.messaging().sendToDevice(array, menssagemNotficacao).then(response => {
    	console.log(response.successCount + ' notificacoes foram enviadas simutaneamente');
    	return 0;
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