/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
package com.sun.xml.stream.buffer.stax;


import com.sun.xml.stream.buffer.AbstractProcessor;
import com.sun.xml.stream.buffer.XMLStreamBuffer;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StreamReaderBufferProcessor extends AbstractProcessor implements XMLStreamReader {
    protected int _eventType;
    protected int _internalEventType;
    protected char[] _characters;
    String uri = "";
    String localName = "";
    String prefix = "";
    
    public StreamReaderBufferProcessor() {
    }
    
    public StreamReaderBufferProcessor(XMLStreamBuffer buffer) {
        this();
        setBuffer(buffer);
    }
    
    public Object getProperty(java.lang.String name) {
        return null;
    }
    
    public int next() throws XMLStreamException {
        int item = readStructure();
        _internalEventType = item;
        switch(item) {
            
            case STATE_ELEMENT_U_LN_QN:{
                _eventType = START_ELEMENT;
                break;
            }
            case STATE_ELEMENT_P_U_LN:{
                _eventType = START_ELEMENT;
                break;
            }
            case STATE_ELEMENT_U_LN: {
                _eventType = START_ELEMENT;
                break;
            }
            case STATE_ELEMENT_LN: {
                _eventType = START_ELEMENT;
                break;
            }
            case STATE_NAMESPACE_ATTRIBUTE_P_U:{
                _eventType = NAMESPACE;
                break;
            }
            case STATE_NAMESPACE_ATTRIBUTE_U:{
                _eventType = NAMESPACE;
                break;
            }
            case STATE_ATTRIBUTE_U_LN_QN:{
                _eventType = ATTRIBUTE;
                break;
            }
            case STATE_ATTRIBUTE_P_U_LN:{
                _eventType = ATTRIBUTE;
                break;
            }
            case STATE_ATTRIBUTE_U_LN : {
                _eventType = ATTRIBUTE;
                break;
            }
            case STATE_ATTRIBUTE_LN: {
                _eventType = ATTRIBUTE;
                break;
            }
            case STATE_TEXT_AS_CHAR_ARRAY:{
                _eventType = CHARACTERS;
                break;
            }
            case STATE_TEXT_AS_STRING:{
                _eventType = CHARACTERS;
                break;
            }
            case STATE_COMMENT_AS_CHAR_ARRAY:{
                _eventType = COMMENT;
                break;
            }
            case STATE_COMMENT_AS_CHAR_ARRAY_COPY:{
                _eventType = COMMENT;
                break;
            }
            case STATE_PROCESSING_INSTRUCTION:{
                _eventType = PROCESSING_INSTRUCTION;
                break;
            }
            case STATE_END:{
                _eventType = END_ELEMENT;
                break;
            }
//            case STATE_END_DOCUMENT:{
//                _eventType = END_DOCUMENT;
//                break;
//            }
            default:{
                throw new XMLStreamException("Invalid State "+item);
            }
        }
        return _eventType;
    }
    
    public final void require(int type, String namespaceURI, String localName)
    throws XMLStreamException {
        if( type != _eventType) {
            throw new XMLStreamException("");
        }
        if( namespaceURI != null && !namespaceURI.equals(getNamespaceURI())) {
            throw new XMLStreamException("");
        }
        if(localName != null && !localName.equals(getLocalName())) {
            throw new XMLStreamException("");
        }
    }
    
    public final String getElementText() throws XMLStreamException {
        if(getEventType() != START_ELEMENT) {
            throw new XMLStreamException("");
        }
        
        //current is StartElement, move to the next
        int eventType = next();
        return getElementText(true);
    }
    
    public final String getElementText(boolean startElementRead) throws XMLStreamException {
        if (!startElementRead) {
            throw new XMLStreamException("");
        }
        
        int eventType = getEventType();
        StringBuffer content = new StringBuffer();
        while(eventType != END_ELEMENT ) {
            if(eventType == CHARACTERS
                    || eventType == CDATA
                    || eventType == SPACE
                    || eventType == ENTITY_REFERENCE) {
                content.append(getText());
            } else if(eventType == PROCESSING_INSTRUCTION
                    || eventType == COMMENT) {
                // skipping
            } else if(eventType == END_DOCUMENT) {
                throw new XMLStreamException("");
            } else if(eventType == START_ELEMENT) {
                throw new XMLStreamException("");
            } else {
                throw new XMLStreamException("");
            }
            eventType = next();
        }
        return content.toString();
    }
    
    public final int nextTag() throws XMLStreamException {
        int eventType = next();
        return nextTag(true);
    }
    
    public final int nextTag(boolean currentTagRead) throws XMLStreamException {
        int eventType = getEventType();
        if (!currentTagRead) {
            eventType = next();
        }
        while((eventType == CHARACTERS && isWhiteSpace()) // skip whitespace
        || (eventType == CDATA && isWhiteSpace())
        || eventType == SPACE
                || eventType == PROCESSING_INSTRUCTION
                || eventType == COMMENT) {
            eventType = next();
        }
        if (eventType != START_ELEMENT && eventType != END_ELEMENT) {
            throw new XMLStreamException("");
        }
        return eventType;
    }
    
    public final boolean hasNext() throws XMLStreamException {
        return (_eventType != END_DOCUMENT);
    }
    
    public void close() throws XMLStreamException {
    }
    
    public final String getNamespaceURI(String prefix) {
        return null;
    }
    
    public final boolean isStartElement() {
        return (_eventType == START_ELEMENT);
    }
    
    public final boolean isEndElement() {
        return (_eventType == END_ELEMENT);
    }
    
    public final boolean isCharacters() {
        return (_eventType == CHARACTERS);
    }
    
    public final boolean isWhiteSpace() {
        if(isCharacters() || (_eventType == CDATA)){
            char [] ch = this.getTextCharacters();
            int start = this.getTextStart();
            int length = this.getTextLength();
            for (int i=start; i< length;i++){
                // if(!XMLChar.isSpace(ch[i])){
                return false;
                // }
            }
            return true;
        }
        return false;
    }
    
    public final String getAttributeValue(String namespaceURI, String localName) {
        if (_eventType != START_ELEMENT) {
            throw new IllegalStateException("");
        }
        
        return null;
    }
    
    public final int getAttributeCount() {
        if (_eventType != START_ELEMENT) {
            throw new IllegalStateException("");
        }
        
        return -1;
    }
    
    public final javax.xml.namespace.QName getAttributeName(int index) {
        if (_eventType != START_ELEMENT) {
            throw new IllegalStateException("");
        }
        
        return null;
    }
    
    public final String getAttributeNamespace(int index) {
        if (_eventType != START_ELEMENT) {
            throw new IllegalStateException("");
        }
        
        return null;
    }
    
    public final String getAttributeLocalName(int index) {
        if (_eventType != START_ELEMENT) {
            throw new IllegalStateException("");
        }
        
        return null;
    }
    
    public final String getAttributePrefix(int index) {
        if (_eventType != START_ELEMENT) {
            throw new IllegalStateException("");
        }
        
        return null;
    }
    
    public final String getAttributeType(int index) {
        if (_eventType != START_ELEMENT) {
            throw new IllegalStateException("");
        }
        
        return null;
    }
    
    public final String getAttributeValue(int index) {
        if (_eventType != START_ELEMENT) {
            throw new IllegalStateException("");
        }
        
        return null;
    }
    
    public final boolean isAttributeSpecified(int index) {
        return false;
    }
    
    public final int getNamespaceCount() {
        if (_eventType == START_ELEMENT || _eventType == END_ELEMENT) {
            return -1;
        }
        
        throw new IllegalStateException("");
    }
    
    public final String getNamespacePrefix(int index) {
        if (_eventType == START_ELEMENT || _eventType == END_ELEMENT) {
            return null;
        }
        
        throw new IllegalStateException("");
    }
    
    public final String getNamespaceURI(int index) {
        if (_eventType == START_ELEMENT || _eventType == END_ELEMENT) {
            return null;
        }
        
        throw new IllegalStateException("");
    }
    
    public final NamespaceContext getNamespaceContext() {
        return null;
    }
    
    public final int getEventType() {
        return _eventType;
    }
    
    public final String getText() {
        return null;
    }
    
    public final char[] getTextCharacters() {
        return null;
    }
    
    public final int getTextStart() {
        return -1;
    }
    
    public final int getTextLength() {
        return -1;
    }
    
    public final int getTextCharacters(int sourceStart, char[] target,
            int targetStart, int length) throws XMLStreamException {
        return -1;
    }
    
    public final String getEncoding() {
        return null;
    }
    
    public final boolean hasText() {
        return (_characters != null);
    }
    
    public final Location getLocation() {
        return null;
    }
    
    public final QName getName() {
        if (_eventType == START_ELEMENT || _eventType == END_ELEMENT) {
            return null;
        }
        
        throw new IllegalStateException("");
    }
    
    public final String getLocalName() {
        if(localName.length() >0){
            return localName;
        }
        if ( getEventType() == START_ELEMENT ||  getEventType() == END_ELEMENT) {
            switch(_internalEventType) {
                
                case STATE_ELEMENT_U_LN_QN:{
                    uri = readStructureString();
                    localName = readStructureString();
                    prefix = getPrefixFromQName(readStructureString());
                    break;
                }
                case STATE_ELEMENT_P_U_LN:{
                    prefix = readStructureString();
                    uri = readStructureString();
                    localName = readStructureString();
                    break;
                }
                case STATE_ELEMENT_U_LN: {
                    uri = readStructureString();
                    localName = readStructureString();
                    break;
                }
                case STATE_ELEMENT_LN: {
                    localName = readStructureString();
                    break;
                }
            }
        }
        return localName;
    }
    
    public final boolean hasName() {
        return (_eventType == START_ELEMENT || _eventType == END_ELEMENT);
    }
    
    public final String getNamespaceURI() {
        if(uri.length() > 0){
            return  uri;
        }
        if (_eventType == START_ELEMENT || _eventType == END_ELEMENT) {
             switch(_internalEventType) {
                
                case STATE_ELEMENT_U_LN_QN:{
                    uri = readStructureString();
                    localName = readStructureString();
                    prefix = getPrefixFromQName(readStructureString());
                    break;
                }
                case STATE_ELEMENT_P_U_LN:{
                    prefix = readStructureString();
                    uri = readStructureString();
                    localName = readStructureString();
                    break;
                }
                case STATE_ELEMENT_U_LN: {
                    uri = readStructureString();
                    localName = readStructureString();
                    break;
                }
                case STATE_ELEMENT_LN: {
                    localName = readStructureString();
                    break;
                }
            }
        }
        
        return uri;
    }
    
    public final String getPrefix() {
        if(prefix.length() >0){
            return prefix;
        }        
        if (_eventType == START_ELEMENT || _eventType == END_ELEMENT) {
            switch(_internalEventType) {
                
                case STATE_ELEMENT_U_LN_QN:{
                    uri = readStructureString();
                    localName = readStructureString();
                    prefix = getPrefixFromQName(readStructureString());
                    break;
                }
                case STATE_ELEMENT_P_U_LN:{
                    prefix = readStructureString();
                    uri = readStructureString();
                    localName = readStructureString();
                    break;
                }
                case STATE_ELEMENT_U_LN: {
                    uri = readStructureString();
                    localName = readStructureString();
                    break;
                }
                case STATE_ELEMENT_LN: {
                    localName = readStructureString();
                    break;
                }
            }
        }
        return prefix;      
        
    }
    
    public final String getVersion() {
        return null;
    }
    
    public final boolean isStandalone() {
        return false;
    }
    
    public final boolean standaloneSet() {
        return false;
    }
    
    public final String getCharacterEncodingScheme() {
        return null;
    }
    
    public final String getPITarget() {
        if (_eventType == PROCESSING_INSTRUCTION) {
            return null;
        }
        
        throw new IllegalStateException("");
    }
    
    public final String getPIData() {
        if (_eventType == PROCESSING_INSTRUCTION) {
            return null;
        }
        
        throw new IllegalStateException("");
    }
}
