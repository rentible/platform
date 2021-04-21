package hu.lanoga.flatshares.converter;

import lombok.extern.slf4j.Slf4j;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//https://stackoverflow.com/questions/20565829/how-to-get-selected-date-with-current-timestamp-in-primefaces
@Slf4j
@FacesConverter("timestampConverter")
public class TimestampConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); //TODO proper format in xhtml
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        try {
            date = sdf.parse(s);
            calendar.setTime(date);
        } catch (ParseException e) {
            log.error("Wrong date's format!", e);
        }
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, now.get(Calendar.SECOND));

        return new Timestamp(calendar.getTime().getTime());
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }
}
