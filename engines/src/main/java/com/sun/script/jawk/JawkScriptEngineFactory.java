/*
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved. 
 * Use is subject to license terms.
 *
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met: Redistributions of source code 
 * must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of 
 * conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution. Neither the name of the Sun Microsystems nor the names of 
 * is contributors may be used to endorse or promote products derived from this software 
 * without specific prior written permission. 

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY 
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER 
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * JawkScriptEngineFactory.java
 * @author A. Sundararajan
 */

package com.sun.script.jawk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org.jawk.Awk;

/**
 * <p>Title: JawkScriptEngineFactory</p>
 * <p>Description: ScriptEngineFactory implementation for <a href="http://jawk.sourceforge.net/">Jawk</a>.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>com.sun.script.jawk.JawkScriptEngineFactory</code></p>
 */
public class JawkScriptEngineFactory implements ScriptEngineFactory {
    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getEngineName()
     */
	@Override
    public String getEngineName() { 
        return "jawk";
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getEngineVersion()
     */
	@Override
    public String getEngineVersion() {
        return Awk.class.getPackage().getImplementationVersion();
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getExtensions()
     */
    @Override
	public List<String> getExtensions() {
        return extensions;
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getLanguageName()
     */
    @Override
	public String getLanguageName() {
        return "awk";
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getLanguageVersion()
     */
    @Override
	public String getLanguageVersion() {
        return "Awk as specified in OpenGroup's single UNIX spec. Version 2";
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getMethodCallSyntax(java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
        StringBuilder buf = new StringBuilder();
        buf.append(obj);
        buf.append(".");
        buf.append(m);
        buf.append("(");
        if (args.length != 0) {
            int i = 0;
            for (; i < args.length - 1; i++) {
                buf.append(args[i] + ", ");
            }
            buf.append(args[i]);
        }        
        buf.append(")");
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getMimeTypes()
     */
    @Override
	public List<String> getMimeTypes() {
        return mimeTypes;
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getNames()
     */
    @Override
	public List<String> getNames() {
        return names;
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getOutputStatement(java.lang.String)
     */
    @Override
	public String getOutputStatement(String toDisplay) {
        StringBuilder buf = new StringBuilder();
        buf.append("printf(\"");
        int len = toDisplay.length();
        for (int i = 0; i < len; i++) {
            char ch = toDisplay.charAt(i);
            switch (ch) {
            case '"':
                buf.append("\\\"");
                break;
            case '\\':
                buf.append("\\\\");
                break;
            default:
                buf.append(ch);
                break;
            }
        }
        buf.append("\")");
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getParameter(java.lang.String)
     */
    @Override
	public String getParameter(String key) {
        if (key.equals(ScriptEngine.ENGINE)) {
            return getEngineName();
        } else if (key.equals(ScriptEngine.ENGINE_VERSION)) {
            return getEngineVersion();
        } else if (key.equals(ScriptEngine.NAME)) {
            return getEngineName();
        } else if (key.equals(ScriptEngine.LANGUAGE)) {
            return getLanguageName();
        } else if (key.equals(ScriptEngine.LANGUAGE_VERSION)) {
            return getLanguageVersion();
        } else if (key.equals("THREADING")) {
            return "THREAD-ISOLATED";
        } else {
            return null;
        }
    } 

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getProgram(java.lang.String[])
     */
    @Override
	public String getProgram(String... statements) {
        StringBuilder buf = new StringBuilder();
        for (String statement: statements) {
            buf.append(statement);
            buf.append(";\n");
        }
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     * @see javax.script.ScriptEngineFactory#getScriptEngine()
     */
    @Override
	public ScriptEngine getScriptEngine() {
        return new JawkScriptEngine(this);
    }

    private static List<String> names;
    private static List<String> extensions;
    private static List<String> mimeTypes;
    static {
        names = new ArrayList<String>(2);
        names.add("jawk");
        names.add("awk");
        names = Collections.unmodifiableList(names);
        extensions = names;
        mimeTypes = new ArrayList<String>(0);
        mimeTypes = Collections.unmodifiableList(mimeTypes);
    }
}
