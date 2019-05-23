package revolution.ph.developer.remediofacilmanaus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

import static revolution.ph.developer.remediofacilmanaus.MainActivity.MEU_CANAL;
import static revolution.ph.developer.remediofacilmanaus.FragmentMain.ADMINISTRADOR;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //getSharedPreferences(Constantes.TOKEN_NOTIFICACAO, MODE_PRIVATE).edit().putString(Constantes.TOKEN, s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        notificacaoSimple(remoteMessage);
    }


    private void notificacaoSimple(RemoteMessage message) {

        String title = message.getData().get("title");
        String body = message.getData().get("body");
        String action = message.getData().get("clickAction");

        Intent intent = new Intent(this, MensagemDetalheActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent intentCentralCompras = new Intent(this, CentralComprasActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent intentMinhasCompras = new Intent(this, MinhasComprasActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent centralCompras = PendingIntent.getActivity(this, 1, intentCentralCompras, 0);
        PendingIntent minhasCompras = PendingIntent.getActivity(this, 1, intentMinhasCompras, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MEU_CANAL);

        builder.setSmallIcon(R.drawable.ic_star_black_24dp);
        builder.setColor(Color.RED);
        builder.setContentTitle(title);
        builder.setContentText(body);

        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);
        if (action.equals("mensagem")) {
            if (ADMINISTRADOR) {
                builder.setContentIntent(centralCompras);
            } else {
                builder.setContentIntent(pendingIntent);
            }
            //todo abrir central de mensagens quando for a compilacao do admin
        } else if (action.equals("compra")) {
            //todo abrir central de compras quando for a compilacao do admin
        } else if (action.equals("statusCompra")){
            builder.setContentIntent(minhasCompras);
        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

    }
}
