package com.wicoder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class Message<T> {

    List<Fields> msm=  new ArrayList<>();

    public ResponseEntity  error(List<ObjectError> fieldError){

        for (Object object : fieldError) {
            if(object instanceof FieldError) {
                FieldError fieldErrors = (FieldError) object;
                msm.add(new Fields(fieldErrors.getField(),fieldErrors.getDefaultMessage()));
            }
        }
        return new ResponseEntity(new Errores(msm,"ERROR"), HttpStatus.BAD_REQUEST);
    }
    public static Object show( String message){

         class Messages {
            private String message;

             public String getMessage() {
                 return message;
             }

             public Messages(String message) {
                this.message = message;
            }
        }
        return  new Messages(message);
    }


    public class Errores {

        private List<Fields> response;
        private String status="OK";
        public Errores(List<Fields> fields) {
            this.response = fields;
        }

        public List<Fields> getResponse() {
            return response;
        }

        public String getStatus() {
            return status;
        }

        public Errores(List<Fields> fields, String status) {
            this.response = fields;
            this.status = status;
        }

    }


    public class Fields {

        private String fieldName;
        private String fieldMessage;

        public String getFieldName() {
            return fieldName;
        }

        public String getFieldMessage() {
            return fieldMessage;
        }

        public Fields(String fieldName, String fieldMessage) {
            this.fieldName = fieldName;
            this.fieldMessage = fieldMessage;
        }
        public Fields(){

        }

    }
}
