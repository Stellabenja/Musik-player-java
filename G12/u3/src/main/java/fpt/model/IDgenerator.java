package fpt.model;

/**
 * Created by benja on 07.06.2017.
 */
public class IDgenerator {
    private static final long idStart = 0;
    private static final long idEnde = 9999;
    private static Model model ;

    public static void init(Model model1){
        model= model1;
    }

    public static Long getNextID() throws IDOverFlowException {
        if (model==null) throw new IDOverFlowException();
        long id = idStart;
        while (model.findSongById(id) != null) {
            id++;
        }
        if (id > idEnde) throw new IDOverFlowException();
        return id;
    }
}
