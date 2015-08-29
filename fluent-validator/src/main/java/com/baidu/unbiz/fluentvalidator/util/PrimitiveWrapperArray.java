/**
 *
 */
package com.baidu.unbiz.fluentvalidator.util;

import com.baidu.unbiz.fluentvalidator.able.TransformTo;
import com.baidu.unbiz.fluentvalidator.able.Valuable;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年9月22日 下午11:17:44
 */
public enum PrimitiveWrapperArray implements Valuable<Class<?>>, TransformTo<Class<?>> {

    PRIMITIVE_BOOLEAN {
        @Override
        public Class<?> value() {
            return boolean[].class;
        }

        @Override
        public Class<?> transform() {
            return Boolean[].class;
        }
    },
    PRIMITIVE_BYTE {
        @Override
        public Class<?> value() {
            return byte[].class;
        }

        @Override
        public Class<?> transform() {
            return Byte[].class;
        }
    },
    PRIMITIVE_CHAR {
        @Override
        public Class<?> value() {
            return char[].class;
        }

        @Override
        public Class<?> transform() {
            return Character[].class;
        }
    },
    PRIMITIVE_SHORT {
        @Override
        public Class<?> value() {
            return short[].class;
        }

        @Override
        public Class<?> transform() {
            return Short[].class;
        }
    },
    PRIMITIVE_INT {
        @Override
        public Class<?> value() {
            return int[].class;
        }

        @Override
        public Class<?> transform() {
            return Integer[].class;
        }
    },
    PRIMITIVE_LONG {
        @Override
        public Class<?> value() {
            return long[].class;
        }

        @Override
        public Class<?> transform() {
            return Long[].class;
        }
    },
    PRIMITIVE_FLOAT {
        @Override
        public Class<?> value() {
            return float[].class;
        }

        @Override
        public Class<?> transform() {
            return Float[].class;
        }
    },
    PRIMITIVE_DOUBLE {
        @Override
        public Class<?> value() {
            return double[].class;
        }

        @Override
        public Class<?> transform() {
            return Double[].class;
        }
    },

    WRAPPER_BOOLEAN {
        @Override
        public Class<?> value() {
            return Boolean[].class;
        }

        @Override
        public Class<?> transform() {
            return boolean[].class;
        }
    },
    WRAPPER_BYTE {
        @Override
        public Class<?> value() {
            return Byte[].class;
        }

        @Override
        public Class<?> transform() {
            return byte[].class;
        }
    },
    WRAPPER_CHAR {
        @Override
        public Class<?> value() {
            return Character[].class;
        }

        @Override
        public Class<?> transform() {
            return char[].class;
        }
    },
    WRAPPER_SHORT {
        @Override
        public Class<?> value() {
            return Short[].class;
        }

        @Override
        public Class<?> transform() {
            return short[].class;
        }
    },
    WRAPPER_INT {
        @Override
        public Class<?> value() {
            return Integer[].class;
        }

        @Override
        public Class<?> transform() {
            return int[].class;
        }
    },
    WRAPPER_LONG {
        @Override
        public Class<?> value() {
            return Long[].class;
        }

        @Override
        public Class<?> transform() {
            return long[].class;
        }
    },
    WRAPPER_FLOAT {
        @Override
        public Class<?> value() {
            return Float[].class;
        }

        @Override
        public Class<?> transform() {
            return float[].class;
        }
    },
    WRAPPER_DOUBLE {
        @Override
        public Class<?> value() {
            return Double[].class;
        }

        @Override
        public Class<?> transform() {
            return double[].class;
        }
    };

    public static PrimitiveWrapperArray find(Class<?> clazz) {
        for (PrimitiveWrapperArray primitiveArray : PrimitiveWrapperArray.values()) {
            if (primitiveArray.value() == clazz) {
                return primitiveArray;
            }
        }

        return null;
    }

}
