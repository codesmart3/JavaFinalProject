package javaProgramming.finalproject;

import java.util.*;
import java.io.*;

public class LinkedList<T> {
	
	private Node head;
	private Node tail;
	private int size = 0;
	
	class Node{
		private T data;
		private Node link;
		
		private Node(T data, Node link) {
			this.data = data;
			this.link = link;
		}
		
	}
	
	public void add(int index, T data) {
		if(index == 0) {
			final Node newNode = new Node(data, null);
			newNode.data = data;
			newNode.link = head;
			head = newNode;
			
			if(tail == null)
					tail = head;
			
			size++;
		} else {
			final Node newNode = new Node(data, null);
			newNode.data = data;
			
			if(head == null) {
				head = newNode;
				tail = newNode;
			} else {
				tail.link = newNode;
				tail = newNode;
			}
			size++;
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public String getValue(int index) {
		Node temp = head;
		String ret;
		
		if(index == 0) {
			ret = head.data.toString();
			return ret;
		}else if(index > 0 && index < size) {	
			for(int i = 0; i<index; i++) {
				temp = temp.link;
			}	
		}
		
		ret = temp.data.toString();
		
		return ret;
	}
	
	
	
}
