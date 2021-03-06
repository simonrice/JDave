/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jdave.tools;

import jdave.Specification;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Joni Freeman
 */
@RunWith(JMock.class)
public class SpecdoxRunnerTest {
    private Mockery context = new JUnit4Mockery();
    private SpecdoxRunner runner;
    private IDoxFormat format = context.mock(IDoxFormat.class);
    private Formats formats;

    @Before
    public void setUp() throws Exception {
        context.setImposteriser(ClassImposteriser.INSTANCE);
        formats = context.mock(Formats.class);
        System.setProperty(SpecdoxRunner.DIRNAME, "target");
        runner = new SpecdoxRunner(new Formats() {
            @Override
            public IDoxFormat formatFor(String formatName) {
                return format;
            }
        });
    }
    
    @Test
    public void testGeneratesDoxIfSystemPropertyPresent() {
        System.setProperty(SpecdoxRunner.FORMAT, "txt");
        context.checking(new Expectations() {{ 
            one(format).newSpec("TestSpec", TestSpec.class.getName());
            one(format).endSpec("TestSpec");
            one(format).newContext("Context");
            one(format).endContext("Context");
            one(format).newBehavior("behavior");
            ignoring(format).suffix();
        }});
        runner.generate(TestSpec.class);
    }
    
    @Test
    public void testParsesFormatsFromSpaceLimitedList() throws Exception {
        runner = new SpecdoxRunner(formats);
        System.setProperty(SpecdoxRunner.FORMAT, "txt xml");
        context.checking(new Expectations() {{ 
            one(formats).formatFor("txt");
            one(formats).formatFor("xml");
        }});
        runner.generate(TestSpec.class);        
    }
    
    public static class TestSpec extends Specification<Void> {
        public class Context {
            public void create() {                
            }
            
            public void behavior() {                
            }
        }
    }
}
