/**
 * Copyright 2016 Netflix, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.hystrix.strategy.properties;

import java.util.ServiceLoader;

/**
 * Hystrix默认提供4个级别的参数配置方式：
 *
 * 全局默认值(Default Value)；Hystrix自身代码默认值，使用方不配置任何参数情况下生效。
 * 可配置全局默认参数(Default Property)；此类配置参数可变更全局默认值。
 * 实例初始值(@Annotation or com.netflix.hystrix.HystrixCommand.Setter.*** or com.netflix.hystrix.HystrixCommandProperties.Setter.***)；熔断器实例初始值，配置此类参数后，不再使用默认值。
 * 可配置实例参数(Instance Property)；可动态调整一个熔断器实例的参数值。
 * 优先级说明：
 *
 * 可配置实例参数(Instance Property) > 实例初始值(@Annotation or com.netflix.hystrix.HystrixCommand.Setter.*** or com.netflix.hystrix.HystrixCommandProperties.Setter.***) > 可配置全局默认参数(Default Property) > 全局默认值(Default Value)；
 *
 * DEBUG:在此类打上断点，调试 {@link com.netflix.hystrix.examples.basic.CommandHelloWorld}
 *
 *
 * 可以通过此SPI实现自己的动态配置
 * A hystrix plugin (SPI) for resolving dynamic configuration properties. This
 * SPI allows for varying configuration sources.
 * 
 * The HystrixPlugin singleton will load only one implementation of this SPI
 * throught the {@link ServiceLoader} mechanism.
 * 
 * @author agentgt
 *
 */
public interface HystrixDynamicProperties {
    
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name, never <code>null</code>
     * @param fallback default value, maybe <code>null</code>
     * @return never <code>null</code>
     */
    public HystrixDynamicProperty<String> getString(String name, String fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name, never <code>null</code>
     * @param fallback default value, maybe <code>null</code>
     * @return never <code>null</code>
     */
    public HystrixDynamicProperty<Integer> getInteger(String name, Integer fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name, never <code>null</code>
     * @param fallback default value, maybe <code>null</code>
     * @return never <code>null</code>
     */
    public HystrixDynamicProperty<Long> getLong(String name, Long fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name
     * @param fallback default value
     * @return never <code>null</code>
     */
    public HystrixDynamicProperty<Boolean> getBoolean(String name, Boolean fallback);
    
    /**
     * @ExcludeFromJavadoc
     */
    public static class Util {
        /**
         * A convenience method to get a property by type (Class).
         * @param properties never <code>null</code>
         * @param name never <code>null</code>
         * @param fallback maybe <code>null</code>
         * @param type never <code>null</code>
         * @return a dynamic property with type T.
         */
        @SuppressWarnings("unchecked")
        public static <T> HystrixDynamicProperty<T> getProperty(
                HystrixDynamicProperties properties, String name, T fallback, Class<T> type) {
            return (HystrixDynamicProperty<T>) doProperty(properties, name, fallback, type);
        }
        
        private static HystrixDynamicProperty<?> doProperty(
                HystrixDynamicProperties delegate, 
                String name, Object fallback, Class<?> type) {
            if(type == String.class) {
                return delegate.getString(name, (String) fallback);
            }
            else if (type == Integer.class) {
                return delegate.getInteger(name, (Integer) fallback);
            }
            else if (type == Long.class) {
                return delegate.getLong(name, (Long) fallback);
            }
            else if (type == Boolean.class) {
                return delegate.getBoolean(name, (Boolean) fallback);
            }
            throw new IllegalStateException();
        }
    }
    
}