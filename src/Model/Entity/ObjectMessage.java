package Model.Entity;

import java.util.ArrayList;

public class ObjectMessage {
    private Object object;
    private String type;
    private ArrayList<String> errors;

    public ObjectMessage(Object object, String type){
        this.object = object;
        this.type = type;
        this.errors = new ArrayList<>();
    }

    public Object getObject() {
        return object;
    }

    public String getType() {
        return type;
    }

    public ArrayList<String> getErrors(){
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }

    public void addError(String error){
        this.errors.add(error);
    }
}
