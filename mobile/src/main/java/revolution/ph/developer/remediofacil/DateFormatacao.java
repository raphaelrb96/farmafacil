package revolution.ph.developer.remediofacil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatacao {

    public static String getDiaString(Date date) {

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        return fmt.format(date);

    }

    public static boolean isHoje(String hm, String ha) {

        if (hm.equals(ha)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getHoraString(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        return fmt.format(date);
    }

    public static String dataCompletaCorrigida(Date horaMensagem, Date horaAtual) {

        String hm = getDiaString(horaMensagem);
        String ha = getDiaString(horaAtual);
        String hh = getHoraString(horaMensagem);

        if (isHoje(hm, ha)) {
            return "Hoje, " + hh;
        } else {
            return hm + ",  " + hh;
        }

    }

}
