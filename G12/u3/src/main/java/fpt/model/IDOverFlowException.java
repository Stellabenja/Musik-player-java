package fpt.model;
/**
 * Created by benja on 07.06.2017.
 */
public class IDOverFlowException extends Exception {
    IDOverFlowException(){
        super();
    }
    public String toString(){
        return "KEIN ID MEHR ZU GEBEN";
    }
}
