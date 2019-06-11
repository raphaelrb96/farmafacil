package revolution.ph.developer.remediofacilmanaus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatacao {

    public static String getDiaString(Date date) {

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yy");
        return fmt.format(date);

    }

    public static String getDiaStringSmall(Date date) {

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM");
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

    public static String dataCompletaCorrigidaSmall(Date horaMensagem, Date horaAtual) {

        String hm = getDiaStringSmall(horaMensagem);
        String ha = getDiaStringSmall(horaAtual);
        String hh = getHoraString(horaMensagem);

        if (isHoje(hm, ha)) {
            return "Hoje\n" + hh;
        } else {
            return hm + "\n" + hh;
        }

    }

    public static String dataCompletaCorrigidaSmall2(Date horaMensagem, Date horaAtual) {

        String hm = getDiaStringSmall(horaMensagem);
        String ha = getDiaStringSmall(horaAtual);
        String hh = getHoraString(horaMensagem);

        if (isHoje(hm, ha)) {
            return "Hoje as " + hh;
        } else {
            return hm + " as " + hh;
        }

    }

}
