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
import org.jmock.integration.junit3.MockObjectTestCase;

/**
 * @author Joni Freeman
 */
public class SpecdoxRunnerTest extends MockObjectTestCase {
    private SpecdoxRunner runner;
    private IDoxFormat format = mock(IDoxFormat.class);

    @Override
    protected void setUp() throws Exception {
        System.setProperty(SpecdoxRunner.DIRNAME, "target");
        runner = new SpecdoxRunner() {
            @Override
            protected IDoxFormat formatFor(String formatName) {
                return format;
            }
        };
    }
    
    public void testGeneratesDoxIfSystemPropertyPresent() {
        System.setProperty(SpecdoxRunner.FORMAT, "txt");
        checking(new Expectations() {{ 
            one(format).newSpec("TestSpec");
            one(format).newContext("Context");
            one(format).newBehavior("behavior");
            ignoring(format).suffix();
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
