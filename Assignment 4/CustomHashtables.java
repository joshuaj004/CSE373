// Joshua Johnson CSE 373

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CustomHashtables {
	public class SeparateChainingHashtable<Key, Value> implements CustomHashInterface<Key, Value> {
		int size;
		int tableSize;
		double rehashingThreshold;
		ArrayList<SeparateChain<Key, Value>> hashtable;
		Set<Key>setOfKeys;
		
		/**
		 * This is the constructor for the chaining hashtable. It creates 
		 * an arrayList where the elements are SeparateChain objects.
		 * 
		 * @param tableSize This is the desired size of the table.
		 * @param rehashingThreshold This is the ratio before the table has to be resized.
		 */
		public SeparateChainingHashtable(int tableSize, double rehashingThreshold){
			this.tableSize = tableSize;
			this.rehashingThreshold = rehashingThreshold;
			this.hashtable = new ArrayList<SeparateChain<Key, Value>>(tableSize);
			for (int i = 0; i < tableSize; i++) {
				this.hashtable.add(null);
			}
			this.size = 0;
			this.setOfKeys = new HashSet<Key>();
		}

		/**
		 * Returns the size of the hashtable, where size is the number of elements within.
		 */
		public int size() {
			return this.size;
		}

		/**
		 * Returns the table size. 
		 */
		public int getTableSize() {
			return this.tableSize;
		}

		/**
		 * Puts a key, value pair in the hashtable. If the index is empty, a new SeparateChain object
		 * is made. If there is an element, it is chained on. Returns a HashPutResponseItem.
		 */
		public HashPutResponseItem putAndMore(Key k, Value v){
			// The location field should be given as its value the 
			// index of the array or ArrayList where that key's separate chain can be found.
			int hash = k.hashCode();
			int location = myMod(hash, this.tableSize);
			if (this.hashtable.get(location) == null) {
				this.hashtable.add(location, new SeparateChain<Key, Value>());
			}
			SeparateChain tempNode = this.hashtable.get(location);
			HashPutResponseItem response = tempNode.insert(k, v);
			if (!response.replaced) {
				this.size++;
			}			
			this.setOfKeys.add(k);
			response.index = location;
			return response;
		}

		/**
		 * Clears the hashtable and resets size to 0.
		 */
		public void clear() {
			this.hashtable = new ArrayList<SeparateChain<Key, Value>>();
			this.size = 0;
		}

		/**
		 * Searches the hashtable for the inputted key. If found, it returns a HashGetResponseItem 
		 * indicating the value, else it returns a HashGetResponseItem with a null value.
		 */
		public HashGetResponseItem<Value> getAndMore(Key key) {
			int hash = key.hashCode();
			int location = myMod(hash, this.tableSize);
			SeparateChain tempNode = this.hashtable.get(location);
			if (tempNode == null) {
				HashGetResponseItem result = new HashGetResponseItem(null, 0, location, false);
				return result;
			}
			HashGetResponseItem result = tempNode.find(key);
			result.index = location;
			return result;
		}

		/**
		 * Gets the lamda ratio (size/tableSize) for this hashtable. 
		 */
		public double getLambda(){
			return this.size/this.tableSize;
		}

		/**
		 * Returns a set of keys for all elements in the hashtable.
		 */
		public Set<Key> keySet() {
			return this.setOfKeys;
		}
		/* Not required but useful in debugging:
		@Override
		public String toString() {
			return "";
		}
		 */

		/* Part of an extra-credit option is to implement this:
		void rehash() {}
		 */
	}
	// Stub for the extra-credit option on linear probing:
	//public class LinearProbingHashtable<Key, Value> implements CustomHashInterface<Key, Value> {

	// If you implement LinearProbingHashtable, automatically rehash, when necessary, 
	// to a newTableSize of the smallest prime that is greater than 2*tableSize.


	/* Useful for turning int value into hash table index values.
	 * Avoids the b4ug where % can return a negative value.
	 */
	public int myMod(int a, int b) {
		return ((a % b) + b) % b; 
	}

	/* primeTest is useful in implementing rehashing.
	 * It takes an int and returns true if it is prime, and false otherwise.
	 */
	public boolean primeTest(int n) {
		if (n < 2) { return false; }
		if (n==2 || n==3) { return true; }
		if ((n % 2) == 0 || (n % 3) == 0) { return false; }
		int limit = (int) Math.ceil(Math.sqrt(n))+1;
		for (int k=5; k <= limit; k+=2) {
			if (n % k == 0) { 
				return false; }
		}
		return true;
	}

}
