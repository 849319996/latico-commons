package com.catt.commons.groovy;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GroovyScriptEngineUtilsTest {

    @Test
    public void testGroovyScriptEngine() throws IOException, ResourceException, groovy.util.ScriptException {

        String url = new File("file/GroovyHello.groovy").getAbsolutePath();
        Map<String, Object> map = new HashMap<>();
        map.put("$name", "abc");

        Object result = null;
        try {
            result = GroovyScriptEngineUtils.evaluate(url, "GroovyHello.groovy", map);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}