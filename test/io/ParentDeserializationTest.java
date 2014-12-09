package io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParentDeserializationTest {

	public static void main(String[] args) {
		try {
			System.out.println("Creating...");
			Child c = new Child(1);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			c.field = 10;
			System.out.println("Serializing...");
			System.out.println("c1.field=" + c.field);
			oos.writeObject(c);
			oos.flush();
			baos.flush();
			oos.close();
			baos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			System.out.println("Deserializing...");
			Child c1 = (Child) ois.readObject();
			System.out.println("c1.i=" + c1.getI());
			System.out.println("c1.field=" + c1.field);
			System.out.println("c1.field2=" + c1.getField2());
			System.out.println("c1.aaa=" + c1.getAaa());
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public static class Parent implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6660885051292349553L;

		protected int field;

		final transient private int field2 = 1000;
		
		final transient private List<String> aaa = new ArrayList<String>();

		protected Parent() {
			field = 5;
			System.out.println("Parent::Constructor");
		}

		public int getField() {
			return field;
		}

		public int getField2() {
			return field2;
		}

		public void setField2(int field2) {
//			this.field2 = field2;
		}

		public List<String> getAaa() {
			return aaa;
		}

		public void setAaa(List<String> aaa) {
//			this.aaa = aaa;
		}
	}

	public static class Child extends Parent implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7909105093669377353L;
		protected int i;

		public Child(int i) {
			this.i = i;
			System.out.println("Child::Constructor");
		}

		public int getI() {
			return i;
		}
	}
}