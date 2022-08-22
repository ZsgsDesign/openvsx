/*
 * This file is generated by jOOQ.
 */
package org.eclipse.openvsx.jooq.tables.records;


import org.eclipse.openvsx.jooq.tables.SpringSessionAttributes;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SpringSessionAttributesRecord extends UpdatableRecordImpl<SpringSessionAttributesRecord> implements Record3<String, String, byte[]> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.spring_session_attributes.session_primary_id</code>.
     */
    public void setSessionPrimaryId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.spring_session_attributes.session_primary_id</code>.
     */
    public String getSessionPrimaryId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.spring_session_attributes.attribute_name</code>.
     */
    public void setAttributeName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.spring_session_attributes.attribute_name</code>.
     */
    public String getAttributeName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.spring_session_attributes.attribute_bytes</code>.
     */
    public void setAttributeBytes(byte[] value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.spring_session_attributes.attribute_bytes</code>.
     */
    public byte[] getAttributeBytes() {
        return (byte[]) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, byte[]> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, String, byte[]> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return SpringSessionAttributes.SPRING_SESSION_ATTRIBUTES.SESSION_PRIMARY_ID;
    }

    @Override
    public Field<String> field2() {
        return SpringSessionAttributes.SPRING_SESSION_ATTRIBUTES.ATTRIBUTE_NAME;
    }

    @Override
    public Field<byte[]> field3() {
        return SpringSessionAttributes.SPRING_SESSION_ATTRIBUTES.ATTRIBUTE_BYTES;
    }

    @Override
    public String component1() {
        return getSessionPrimaryId();
    }

    @Override
    public String component2() {
        return getAttributeName();
    }

    @Override
    public byte[] component3() {
        return getAttributeBytes();
    }

    @Override
    public String value1() {
        return getSessionPrimaryId();
    }

    @Override
    public String value2() {
        return getAttributeName();
    }

    @Override
    public byte[] value3() {
        return getAttributeBytes();
    }

    @Override
    public SpringSessionAttributesRecord value1(String value) {
        setSessionPrimaryId(value);
        return this;
    }

    @Override
    public SpringSessionAttributesRecord value2(String value) {
        setAttributeName(value);
        return this;
    }

    @Override
    public SpringSessionAttributesRecord value3(byte[] value) {
        setAttributeBytes(value);
        return this;
    }

    @Override
    public SpringSessionAttributesRecord values(String value1, String value2, byte[] value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SpringSessionAttributesRecord
     */
    public SpringSessionAttributesRecord() {
        super(SpringSessionAttributes.SPRING_SESSION_ATTRIBUTES);
    }

    /**
     * Create a detached, initialised SpringSessionAttributesRecord
     */
    public SpringSessionAttributesRecord(String sessionPrimaryId, String attributeName, byte[] attributeBytes) {
        super(SpringSessionAttributes.SPRING_SESSION_ATTRIBUTES);

        setSessionPrimaryId(sessionPrimaryId);
        setAttributeName(attributeName);
        setAttributeBytes(attributeBytes);
    }
}
