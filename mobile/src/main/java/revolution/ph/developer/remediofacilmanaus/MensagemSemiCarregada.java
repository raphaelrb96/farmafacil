package revolution.ph.developer.remediofacilmanaus;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MensagemSemiCarregada extends MensagemObject {

    private byte[] fotoBruta;
    private Uri uriFoto;
    private int tipoImagemSemi;
    private String nomeFotoSemi;


    public byte[] getFotoBruta() {
        return fotoBruta;
    }

    public Uri getUriFoto() {
        return uriFoto;
    }

    public int getTipoImagemSemi() {
        return tipoImagemSemi;
    }

    public MensagemSemiCarregada(MensagemObject mensagemObject, byte[] fotoBruta, Uri uriFoto, int tipoImagemSemi, String nomeFotoSemi) {
        super(mensagemObject.getTimeStamp(), mensagemObject.getUidUser(), mensagemObject.getTipoMensagem(), mensagemObject.getPathFoto(), mensagemObject.getProdObj(), mensagemObject.getMenssagemText());
        this.fotoBruta = fotoBruta;
        this.uriFoto = uriFoto;
        this.tipoImagemSemi = tipoImagemSemi;
        this.nomeFotoSemi = nomeFotoSemi;
    }

    public String getNomeFotoSemi() {
        return nomeFotoSemi;
    }

    public void imagem(ImageView imageView, Context context) {
        if (tipoImagemSemi == 1) {
            setImagemByte(imageView, context);
        } else {
            setImagemFile(imageView, context);
        }
    }

    public void setImagemByte(ImageView imageView, Context context) {
        Glide.with(context).load(fotoBruta).into(imageView);
    }

    public void setImagemFile(ImageView imageView, Context context) {
        Glide.with(context).load(uriFoto).into(imageView);
    }

}
