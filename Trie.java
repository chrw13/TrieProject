package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() {
	}

	/**
	 * Builds a trie by inserting all words in the input array, one at a time, in
	 * sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words in the
	 * input array are all lower case.
	 * 
	 * @param allWords
	 *            Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/

		if (allWords != null && allWords.length > 0) {
			// Build index for firstNode
			System.out.println("Build Trie for firstNode:	" + allWords[0]);

			Indexes firstIndex = new Indexes(0, 0, 0);
			firstIndex.setEndIndex(allWords[0].length() - 1);
			
			//// Build first Trie Node
			TrieNode firstNode = new TrieNode(firstIndex, null, null);
			
			// Build Root Trie Node
			TrieNode rootNode = new TrieNode(null, firstNode, null);

			for (int i = 1; i < allWords.length; i++) {
				System.out.println("build Trie for " + allWords[i]);
				String currentStr = allWords[i];
				// search Trie and see if any subStr matches prefix of this string
				buildTrieWithNewNode(allWords, rootNode, currentStr, i);

				System.out.println("Finished build with one new node:\n" + rootNode.toString());

			}
			return rootNode;
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		// TrieNode tnode = new TrieNode(index, leftNode, rightNode);

		else
			return null;
	}

	private static void buildTrieWithNewNode(String[] allWords, TrieNode parent, String currentStr,
			int currentWordIndex) {

		int lcp; //largest common prefix position

		// Browse through TrieNode Tree structure
		TrieNode existingNode = parent.getFirstChild();
		if (existingNode != null) {

			lcp = compareTrieNodes(allWords, currentStr, existingNode);
            while (lcp == 0) {
				// no common substr, check sibling
				System.out.println("No common prefix, earching sibling node");

				if (existingNode.getSibling() != null) {
					existingNode = existingNode.getSibling();
					lcp = compareTrieNodes(allWords, currentStr, existingNode);
				} else
					appendExistingNode(allWords, existingNode, currentStr, currentWordIndex);

			}
			// common prefix, update existing node, and build more node
			System.out.println("There is common prefix, update current TreiNode and create sibling");
			updateExistingNode(allWords, existingNode, currentStr, currentWordIndex, lcp);
		} else
			System.out.println("should not be here");
	}



	private static void appendExistingNode(String[] allWords, TrieNode existingNode, String currentStr,
			int currentWordIndex) {

		// create indexes and TrieNode for new currentStr
		Indexes currentStrNodeIndexes = new Indexes(0, 0, 0);
		currentStrNodeIndexes.setWordIndex(currentWordIndex);
		currentStrNodeIndexes.setStartIndex(0);
		currentStrNodeIndexes.setEndIndex(currentStr.length() - 1);

		TrieNode currentStrNode = new TrieNode(null, null, null);
		currentStrNode.setSubstr(currentStrNodeIndexes);

		// connect currentStrNode as sibling as oldNodeChild
		existingNode.setSibling(currentStrNode);

	}

	private static void updateExistingNode(String[] allWords, TrieNode existingNode, String currentStr,
			int currentWordIndex, int lcp) {

		// update existingNode endIndex with largest common prefix value
		Indexes existingSubStrIndexes = existingNode.getSubstr();
		
		// create indexes and TrieNode for oldNode's childNode
		Indexes existingNodeChildIndexes = new Indexes(0, 0, 0);
		existingNodeChildIndexes.setWordIndex(existingSubStrIndexes.getWordIndex());
		existingNodeChildIndexes.setStartIndex(lcp);
		existingNodeChildIndexes.setEndIndex(existingSubStrIndexes.getEndIndex());
		
		existingSubStrIndexes.setEndIndex(lcp-1);
		

		TrieNode existingNodeChildNode = new TrieNode(null, null, null);
		existingNodeChildNode.setSubstr(existingNodeChildIndexes);
		

		


		// create indexes and TrieNode for new currentStr
		Indexes currentStrNodeIndexes = new Indexes(0, 0, 0);
		currentStrNodeIndexes.setWordIndex(currentWordIndex);
		currentStrNodeIndexes.setStartIndex(lcp);
		currentStrNodeIndexes.setEndIndex(currentStr.length() - 1);

		TrieNode currentStrNode = new TrieNode(null, null, null);
		currentStrNode.setSubstr(currentStrNodeIndexes);

		// connect currentStrNode as sibling as oldNodeChild
		existingNodeChildNode.setSibling(currentStrNode);

	}

	private static int compareTrieNodes(String[] allWords, String currentStr, TrieNode childNode) {

		Indexes childSubStrIndexes = childNode.getSubstr();
		int wordIndex = childSubStrIndexes.getWordIndex();
		int subStart = childSubStrIndexes.getStartIndex();
		int subEnd = childSubStrIndexes.getEndIndex();

		String childSubStr = allWords[wordIndex].substring(subStart, subEnd+1);
		String currentSubStr = currentStr.substring(subStart, currentStr.length());

		return findLCPrefix(childSubStr, currentSubStr);
	}

	private static int findLCPrefix(String str1, String str2) {
		int len;
		int i = 0;
		if (str1.length() > str2.length())
			len = str2.length();
		else
			len = str1.length();

		while (str1.charAt(i) == str2.charAt(i) && i < len)
			i++;

		return i;
	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf
	 * nodes in the trie whose words start with this prefix. For instance, if the
	 * trie had the words "bear", "bull", "stock", and "bell", the completion list
	 * for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell";
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and
	 * "bell", and for prefix "bell", completion would be the leaf node that holds
	 * "bell". (The last example shows that an input prefix can be an entire word.)
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be", the
	 * returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root
	 *            Root of Trie that stores all words to search on for completion
	 *            lists
	 * @param allWords
	 *            Array of words that have been inserted into the trie
	 * @param prefix
	 *            Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the
	 *         prefix, order of leaf nodes does not matter. If there is no word in
	 *         the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return null;
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}

	public static void main(String[] args) {
		System.out.println("findLCP:" + findLCPrefix("door", "doom"));
	}
}
