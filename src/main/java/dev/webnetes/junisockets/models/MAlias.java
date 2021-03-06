package dev.webnetes.junisockets.models;

/**
 * MAlias
 */
public class MAlias {
    private String id;
    private boolean accepting;

    /**
     * Constructor for MAlias
     * @param id id 
     * @param accepting accepting
     */
    public MAlias(String id, boolean accepting) {
        this.id = id;
        this.accepting = accepting;
    }
    

    /** 
     * Returns id
     * @return String
     */
    public String getId() {
        return id;
    }

    
    /** 
     * Returns accepting
     * @return boolean
     */
    public boolean getAccepting() {
        return accepting;
    }
}
