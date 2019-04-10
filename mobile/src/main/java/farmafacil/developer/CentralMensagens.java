package farmafacil.developer;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class CentralMensagens {
    private Date time;
    private String uid;

    public CentralMensagens(Date date, String str) {
        this.time = date;
        this.uid = str;
    }

    public CentralMensagens() {
    }

    public String getUid() {
        return this.uid;
    }

    @ServerTimestamp
    public Date getTime() {
        return this.time;
    }
}