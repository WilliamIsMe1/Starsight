package william.starsight.graphics.texture;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A quad-tree based texture atlas
 */
public class TextureAtlas {

    QuadTreeNode root;

    HashMap<String, QuadTreeNode> textures = new HashMap<>();

    public void placeTexture(@NotNull String name, @NotNull BufferedImage contents) {
        if (contents.getWidth() != contents.getHeight()) {
            throw new IllegalArgumentException("The BufferedImage is not square.");
        }
        int sideLength = contents.getWidth();
        if (!((sideLength != 0) && ((sideLength & (sideLength - 1)) == 0))) { // If side length is not a power of 2
            throw new IllegalArgumentException("Side length must be a power of 2!");
        }

//        QuadTreeNode node = searchForSmallestSpace(root, sideLength);
//      Also preserving this.
//        if (node.sideLength < sideLength) throw new IllegalStateException("What da");

        List<QuadTreeNode> leaves = getRecursiveLeaves(root).stream()
                .filter(n -> n.contents == null) // Make sure that the node is empty
                .filter(n -> n.sideLength >= sideLength)
                .sorted() // Sort by size
                .toList();

        var node = leaves.getFirst(); // The smallest is the first element

        /* Split up nodes and dive into first one */
        while (node.sideLength > sideLength) {
            node.isOccupied = true;
            node.subNode1 = new QuadTreeNode(); // Split it up :D
            node.subNode2 = new QuadTreeNode();
            node.subNode3 = new QuadTreeNode();
            node.subNode4 = new QuadTreeNode();

            node.subNode1.sideLength = node.sideLength / 2; // Set side lengths
            node.subNode2.sideLength = node.sideLength / 2;
            node.subNode3.sideLength = node.sideLength / 2;
            node.subNode4.sideLength = node.sideLength / 2;

            node.subNode2.leftX += node.subNode1.sideLength; // Set origin
            node.subNode3.leftX += node.subNode1.sideLength;
            node.subNode3.topY += node.subNode1.sideLength;
            node.subNode4.topY += node.subNode1.sideLength;

            node = node.subNode1;
        }
        node.contents = contents;
        node.isOccupied = true;
        node.name = name;

        textures.put(name, node);
    }

    /*
    I'm preserving this because this was my first attempt at this
    /// Search for smallest node in the thing that is still big enough to fit target
    private QuadTreeNode searchForSmallestSpace(QuadTreeNode node, int target) { // NOTE: This may be too complex. I may just be able to find all leaf nodes and get smallest. This one is bugprone.
        if (node.contents != null) { // If already filled
            return null;
        }
        if (!node.isOccupied) { // If unoccupied by content or subnodes, then check to see if it fits in.
            if (node.sideLength >= target) {
                return node;
            } else return null; // Too small; discard. This shouldn't happen
        }
        var sub1 = searchForSmallestSpace(node.subNode1, target); // Check all subnodes recursively to get to leaves
        var sub2 = searchForSmallestSpace(node.subNode2, target);
        var sub3 = searchForSmallestSpace(node.subNode3, target);
        var sub4 = searchForSmallestSpace(node.subNode4, target);

        // Get smallest of those leaves that fit the target and aren't null.
        List<QuadTreeNode> nodes = List.of(sub1, sub2, sub3, sub4);
        nodes = nodes.stream().filter(Objects::nonNull).sorted().toList(); // .filter may be unnecessary here.
        return nodes.getLast(); // whichever is smallest.
    }
*/
    @NotNull
    @Contract(pure = true)
    private List<QuadTreeNode> getRecursiveLeaves(@NotNull QuadTreeNode node) {
        if (!node.isOccupied || node.contents != null) { // Is a leaf node, either empty or content filled
            return List.of(node);
        }

        ArrayList<QuadTreeNode> pile = new ArrayList<>();
        pile.addAll(getRecursiveLeaves(node.subNode1));
        pile.addAll(getRecursiveLeaves(node.subNode2));
        pile.addAll(getRecursiveLeaves(node.subNode3));
        pile.addAll(getRecursiveLeaves(node.subNode4));

        return pile;
    }

    public static final class QuadTreeNode implements Comparable<QuadTreeNode> {
        private QuadTreeNode subNode1 = null;
        private QuadTreeNode subNode2 = null;
        private QuadTreeNode subNode3 = null;
        private QuadTreeNode subNode4 = null;
        private boolean isOccupied = false; // If either any subnodes are non-null or contents is non-null
        private BufferedImage contents = null; // Will be null if any of the above are not null
        private String name = null;

        public int getLeftX() {
            return leftX;
        }

        public int getTopY() {
            return topY;
        }

        public int getSideLength() {
            return sideLength;
        }

        private int leftX = 0;
        private int topY = 0;
        private int sideLength = 0;

        @Override
        public int compareTo(@NotNull QuadTreeNode o) { // Sorts out the sizes
            return Integer.compare(sideLength, o.sideLength);
        }
    }

    /**
     * Flushes atlas to texture
     *
     * @return The finished UNINITIALIZED texture
     */
    public Texture flushTexture() {
        BufferedImage img = new BufferedImage(root.sideLength, root.sideLength, BufferedImage.TYPE_INT_ARGB); // then I can put this in :D

        List<QuadTreeNode> placements = new ArrayList<>(textures.values());

        var g = img.createGraphics(); // NOTE Could be a VERY temporary solution

        for (QuadTreeNode q : placements) {
            int x = q.leftX;
            int y = q.topY;
            int w = q.sideLength;
            int h = q.sideLength;

            g.drawImage(q.contents, x, y, w, h, null); // Hopefully this smooths over the ARGB and ABGR issues; I'd hate having to deal with something that dumb.
        }

        g.dispose();

        return new BufferedImageBasedTexture(img);
    }
}
