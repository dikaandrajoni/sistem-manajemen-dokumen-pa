package com.proyekakhir.manajemendokumen.core.util;

import com.proyekakhir.manajemendokumen.core.anotation.FormReference;
import com.proyekakhir.manajemendokumen.core.entity.FormSpec;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DtoToFormUtil {
    private Class formObject;

    public DtoToFormUtil(Class formObject) {
        this.formObject = formObject;
    }



    public List<FormSpec> createForm() {
        List<FormSpec> list =  new ArrayList<>();
        try {
            Class cls = this.formObject;

            Field fieldlist[]
                    = cls.getDeclaredFields();
            for (int i
                 = 0; i < fieldlist.length; i++) {
                Field fld = fieldlist[i];
                FormReference formReference =  fld.getAnnotation(FormReference.class);
                String defaultType =  fld.getType().getSimpleName().toLowerCase(Locale.ROOT);
                if(formReference != null) {
                    list.add(new FormSpec(fld.getName(),
                            formReference.label() != "" ? formReference.label()  : fld.getName(),
                            getNotNullOrNotBlankAnnotation(fld),
                            formReference.type() != "" ? formReference.type()  : defaultType,
                            formReference.url() != "" ? formReference.url()  : null)
                    );
                }
            }
        }
        catch (Throwable e) {
            System.err.println(e);
        }

        return  list;
    }

    private Boolean getNotNullOrNotBlankAnnotation (Field fld ) {
        return (fld.isAnnotationPresent(NotNull.class) || fld.isAnnotationPresent(NotBlank.class));
    }
}
