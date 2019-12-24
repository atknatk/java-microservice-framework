package com.esys.framework.base.reader.excel;

import com.esys.framework.base.enums.DataTypeEnum;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
public class ExcelPojoMapper {
    final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Excel verisini Pojo Objesine donusturur
     * @param excelValueConfigs Excel Config Verisi
     * @param clazz Donusturulecek olan Class
     * @param <T> Class Turu
     * @return List of Pojo
     */
    public static <T> List<T> getPojos(List<ExcelValueConfig[]> excelValueConfigs, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        excelValueConfigs.forEach(evc -> {
            T t = null;
            try {
                t = clazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
                e1.printStackTrace();
            }
            Class<? extends Object> classz = t.getClass();
            for (int i = 0; i < evc.length; i++) {
                for (Field field : classz.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (evc[i].getPojoAttribute().equalsIgnoreCase(field.getName())) {
                        try {
                            if (DataTypeEnum.STRING.getValue().equalsIgnoreCase(evc[i].getExcelColType())) {
                                field.set(t, evc[i].getExcelValue());
                            } else if (DataTypeEnum.DOUBLE.getValue().equalsIgnoreCase(evc[i].getExcelColType())) {
                                field.set(t, Double.valueOf(evc[i].getExcelValue()));
                            } else if (DataTypeEnum.INTEGER.getValue().equalsIgnoreCase(evc[i].getExcelColType())) {
                                field.set(t, Double.valueOf(evc[i].getExcelValue()).intValue());
                            } else if (DataTypeEnum.DATE.getValue().equalsIgnoreCase(evc[i].getExcelColType())) {
                                field.set(t, LocalDate.parse(evc[i].getExcelValue(), dtf));
                            } else if (DataTypeEnum.FLOAT.getValue().equalsIgnoreCase(evc[i].getExcelColType())) {
                                field.set(t, Float.valueOf(evc[i].getExcelValue()));
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            list.add(t);
        });
        return list;
    }

}
