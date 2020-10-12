
package multi_io

import org.scalatest.{ Matchers, FlatSpec, GivenWhenThen}

import chisel3._
import chisel3.util._
import chisel3.iotesters._

class Foo extends MultiIOModule {
  val a = IO(Input(UInt(8.W)))
  val z = IO(Output(UInt(8.W)))

  z := a
}

class Foo2 extends Foo {
  val plus1 = IO(Output(UInt(8.W)))
  plus1 := a +% 1.U
}


class Tester( factory: () => Foo) extends GenericTest {
  behavior of s"Foo"
  it should "compile and execute without expect violations" in {
    chisel3.iotesters.Driver.execute( factory, optionsManager) { c =>
      new PeekPokeTester(c) {
        poke( c.a, 1)
        expect( c.z, 1)
      }
    }
  }
}

class Tester2( factory: () => Foo2) extends GenericTest {
  behavior of s"Foo2"
  it should "compile and execute without expect violations" in {
    chisel3.iotesters.Driver.execute( factory, optionsManager) { c =>
      new PeekPokeTester(c) {
        poke( c.a, 1)
        expect( c.z, 1)
        expect( c.plus1, 2)
      }
    }
  }
}

class Test extends Tester( () => new Foo)
class Test2 extends Tester2( () => new Foo2)
