package org.opentox.db.table;

import org.opentox.db.processors.AbstractDbProcessor;


/**
 * A Processor for the manipulation of the tables in the database. Subclasses of
 * {@link AbstractTableProcessor this processor } are responsible for the creation
 * and deletion of new tables in the database.
 * @author Sopasakis Pantelis
 * @author Charalampos Chomenides
 * @see TableCreator
 * @see TableDropper
 */
public abstract class AbstractTableProcessor extends AbstractDbProcessor<Object, Object>{

    private String TABLE_NAME = "";

    private String TABLE_STRUCTURE = "";
    

    public AbstractTableProcessor(){
        super();
    }
    
    public AbstractTableProcessor(final String table_name, final String table_structure){
        this.TABLE_NAME = table_name;
        this.TABLE_STRUCTURE = table_structure;
    }

    public AbstractTableProcessor(final StandardTables TABLE){
        this.TABLE_NAME = TABLE.getTableName();
        this.TABLE_STRUCTURE = TABLE.getTableStructure();
    }

    public String getTableName(){
        return TABLE_NAME;
    }

    public String getTableStructure(){
        return TABLE_STRUCTURE;
    }

    /**
     * An implementation of {@link AbstractDbProcessor#execute(java.lang.Object) execute()}
     * in {@link AbstractDbProcessor }.
     * @param q This parameter has in fact no meaning at all. You can either set it to
     * 'null' or 'new Object()' or whatever you like; it is not used in the body of the method
     * whatsoever.
     * @return null by default
     */
    public abstract Object execute(Object q);


}
