package com.fu.springboot3demo.record;

/**
 * 创建日期：2024-06-26
 */
public record UserRecord() {

    private static void aMethod() {
        //可以在方法内部创建记录，一般不会这样使用。
        record Hobby(String type, String name) {
        }
        //equal1 和 equal2 使用 equals 比较时是 true.因为 record 会重写 equasl 和 HashCode 方法
        Hobby equal1 = new Hobby("球类", "篮球");
        Hobby equal2 = new Hobby("球类", "篮球");
        Hobby hobby1 = new Hobby("球类", "乒乓球");
        Hobby hobby2 = new Hobby("田径类", "短跑");
        Hobby hobby3 = new Hobby("田径类", "长跑");

        //equal1.equals(equal2)结果为true
        if (equal1.equals(equal2)) {

        }

    }
}
