package org.opentox.db.table;

import org.opentox.db.processors.AbstractDbProcessor;


/**
 * A Processor for the manipulation of the tables in the database. Subclasses of
 * {@link AbstractTableProcessor this processor } are responsible for the creation
 * and deletion of new tables in the database.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @see TableCreator
 * @see TableDropper
 */
public abstract class AbstractTableProcessor extends AbstractDbProcessor<Table, Object>{

    /**
     * Create a new Abstract Table Creator. This constructor is intended to be
     * overriden by subclasses that implement AbstractTableCreator.
     */
    public AbstractTableProcessor(){
        super();
    }
   

    /**
     * An implementation of {@link AbstractDbProcessor#execute(java.lang.Object) execute()}
     * in {@link AbstractDbProcessor }.
     * @param q This parameter has in fact no meaning at all. You can either set it to
     * 'null' or 'new Object()' or whatever you like; it is not used in the body of the method
     * whatsoever.
     * @return null by default
     */
    public abstract Object execute(Table q);


}
