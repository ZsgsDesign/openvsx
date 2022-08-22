/*
 * This file is generated by jOOQ.
 */
package org.eclipse.openvsx.jooq.tables;


import java.util.Arrays;
import java.util.List;

import org.eclipse.openvsx.jooq.Keys;
import org.eclipse.openvsx.jooq.Public;
import org.eclipse.openvsx.jooq.tables.records.NamespaceRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Namespace extends TableImpl<NamespaceRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.namespace</code>
     */
    public static final Namespace NAMESPACE = new Namespace();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NamespaceRecord> getRecordType() {
        return NamespaceRecord.class;
    }

    /**
     * The column <code>public.namespace.id</code>.
     */
    public final TableField<NamespaceRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.namespace.name</code>.
     */
    public final TableField<NamespaceRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.namespace.public_id</code>.
     */
    public final TableField<NamespaceRecord, String> PUBLIC_ID = createField(DSL.name("public_id"), SQLDataType.VARCHAR(128), this, "");

    private Namespace(Name alias, Table<NamespaceRecord> aliased) {
        this(alias, aliased, null);
    }

    private Namespace(Name alias, Table<NamespaceRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.namespace</code> table reference
     */
    public Namespace(String alias) {
        this(DSL.name(alias), NAMESPACE);
    }

    /**
     * Create an aliased <code>public.namespace</code> table reference
     */
    public Namespace(Name alias) {
        this(alias, NAMESPACE);
    }

    /**
     * Create a <code>public.namespace</code> table reference
     */
    public Namespace() {
        this(DSL.name("namespace"), null);
    }

    public <O extends Record> Namespace(Table<O> child, ForeignKey<O, NamespaceRecord> key) {
        super(child, key, NAMESPACE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<NamespaceRecord> getPrimaryKey() {
        return Keys.NAMESPACE_PKEY;
    }

    @Override
    public List<UniqueKey<NamespaceRecord>> getKeys() {
        return Arrays.<UniqueKey<NamespaceRecord>>asList(Keys.NAMESPACE_PKEY, Keys.UNIQUE_NAMESPACE_PUBLIC_ID);
    }

    @Override
    public Namespace as(String alias) {
        return new Namespace(DSL.name(alias), this);
    }

    @Override
    public Namespace as(Name alias) {
        return new Namespace(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Namespace rename(String name) {
        return new Namespace(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Namespace rename(Name name) {
        return new Namespace(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
