import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Assignment 10 Problem 1
// Horn Steven
// horns
// Hughes Brian
// hughesbrian

class Cell {
  int x;
  int y;
  ArrayList<Edge> outEdges;
  Cell top;
  Cell bottom;
  Cell right;
  Cell left;
  double length;
  double width;
  Color color = Color.GRAY;

  Cell(int x, int y) {
    this.x = x;
    this.y = y;
    this.outEdges = new ArrayList<Edge>();
    this.top = this;
    this.bottom = this;
    this.right = this;
    this.left = this;
    this.length = MazeWorld.CELL_LENGTH;
    this.width = MazeWorld.CELL_WIDTH;
  }

  // draws the cell
  WorldImage drawCell(ArrayList<ArrayList<Cell>> board) {
    if (board.get(0).get(0).equals(this)) {
      color = Color.GREEN;
    } else if (board.get(MazeWorld.MAZE_X - 1).get(MazeWorld.MAZE_Y - 1).equals(this)) {
      color = Color.MAGENTA;
    }
    WorldImage base = new RectangleImage((int) this.width, (int) this.length, 
        OutlineMode.SOLID, this.color);
    if (!this.isEdge(this.top)) {
      base = new OverlayOffsetImage(new RectangleImage((int) this.width, 1, 
          OutlineMode.SOLID, Color.BLACK), 0,
          (this.length / 2) - .5, base);
    }
    if (!this.isEdge(this.bottom)) {
      base = new OverlayOffsetImage(new RectangleImage((int) this.width, 1, 
          OutlineMode.SOLID, Color.BLACK), 0,
          -(this.length / 2) + .5, base);
    }
    if (!this.isEdge(this.right)) {
      base = new OverlayOffsetImage(new RectangleImage(1, (int) this.length, 
          OutlineMode.SOLID, Color.BLACK),
          (this.width / 2) - .5, 0, base);
    }
    if (!this.isEdge(this.left)) {
      base = new OverlayOffsetImage(new RectangleImage(1, (int) this.length, 
          OutlineMode.SOLID, Color.BLACK),
          -(this.width / 2) + .5, 0, base);
    }
    return base;
  }

  // is there an edge from this cell to that cell
  boolean isEdge(Cell that) {
    for (Edge e : outEdges) {
      if (e.to.equals(that)) {
        return true;
      }
    }
    return false;
  }

  // creates the edges
  void makeEdges(ArrayList<ArrayList<Cell>> b) {
    if (this.x > 0) {
      Cell c = b.get(x - 1).get(y);
      this.right = c;
      this.outEdges.add(new Edge(this, c));
    }
    if (this.y < MazeWorld.MAZE_Y - 1) {
      Cell c = b.get(x).get(y + 1);
      this.bottom = c;
      this.outEdges.add(new Edge(this, c));
    }
    if (this.x < MazeWorld.MAZE_X - 1) {
      Cell c = b.get(x + 1).get(y);
      this.left = c;
      this.outEdges.add(new Edge(this, c));
    }
    if (this.y > 0) {
      Cell c = b.get(x).get(y - 1);
      this.top = c;
      this.outEdges.add(new Edge(this, c));
    }
  }

  // overriding equals
  public boolean equals(Object o) {
    if (o instanceof Cell) {
      Cell c = (Cell) o;
      return this.x == c.x && this.y == c.y;
    } else {
      return false;
    }
  }
  
  // overriding hashCode
  public int hashCode() {
    return this.x + this.y;
  }

  // removes edges
  void removeEdges(ArrayList<Edge> edges) {
    ArrayList<Edge> newList = new ArrayList<Edge>();
    for (Edge e : this.outEdges) {
      if (edges.contains(e)) {
        newList.add(e);
      }
    }
    this.outEdges = newList;
  }

  void setColor(Color c) {
    this.color = c;
  }

  // gets the neighbors of this cell
  ArrayList<Cell> getNeighbors() {
    ArrayList<Cell> list = new ArrayList<Cell>();
    list.add(this.top);
    list.add(this.bottom);
    list.add(this.right);
    list.add(this.left);
    return list;
  }
}

class Player {
  Cell location;
  ArrayList<Cell> cellList = new ArrayList<Cell>();
  String mode;

  Player(Cell cell) {
    this.location = cell;
    cellList.add(location);
  }

  WorldImage drawPlayer() {
    return new RectangleImage((int) location.width, 
        (int) location.length, OutlineMode.SOLID, Color.ORANGE);
  }

  double getX() {
    return ((double) this.location.x);
  }

  double getY() {
    return ((double) this.location.y);
  }

  public void movePlayer(String k, int maxX, int maxY) {
    Cell prev = this.location;
    if (this.getX() == maxX - 1 && this.getY() == maxY - 1) {
      return;
    } else if (k.equals("right")) {
      if (this.location.isEdge(this.location.left)) {
        this.location = this.location.left;
        this.addToPath(this.location, prev);
      }
    } else if (k.equals("left")) {
      if (this.location.isEdge(this.location.right)) {
        this.location = this.location.right;

        this.addToPath(this.location, prev);
      }
    } else if (k.equals("up")) {
      if (this.location.isEdge(this.location.top)) {
        this.location = this.location.top;
        this.addToPath(this.location, prev);
      }
    } else if (k.equals("down")) {
      if (this.location.isEdge(this.location.bottom)) {
        this.location = this.location.bottom;
        this.addToPath(this.location, prev);
      }
    }
  }

  // adds the cell to the path list
  void addToPath(Cell current, Cell prev) {
    this.cellList.add(prev);
    if (this.checkIn(current)) {
      this.removeFromPath();
      prev.setColor(Color.cyan);
      return;
    } else {
      prev.setColor(Color.BLUE);
    }
  }

  // removes the last cell from the path list
  void removeFromPath() {
    this.cellList.remove(this.cellList.size() - 1);
    this.cellList.remove(this.cellList.size() - 1);
  }

  // is the cell in the path list
  boolean checkIn(Cell c) {
    if (this.cellList.size() == 1) {
      return false;
    } else {
      return this.cellList.get(this.cellList.size() - 2).equals(c);
    }
  }
}

class Edge {
  Cell from;
  Cell to;
  int weight;

  Edge(Cell from, Cell to) {
    Random rand = new Random();
    this.from = from;
    this.to = to;
    this.weight = rand.nextInt(10);
  }

  // overriding equals
  public boolean equals(Object o) {
    if (o instanceof Edge) {
      Edge oEdge = (Edge) o;
      return this.from.equals(oEdge.from) && this.to.equals(oEdge.to)
          || this.to.equals(oEdge.from) && this.from.equals(oEdge.to);
    }
    return false;
  }
  
  // overriding hashCode
  public int hashCode() {
    return this.from.hashCode() + this.toString().hashCode() + this.weight;
  }
}

class MazeWorld extends World {
  Player player;
  ArrayList<ArrayList<Cell>> board;
  public static final int MAZE_X = 50;
  public static final int MAZE_Y = 30;
  public static final double CELL_WIDTH = 1000 / MazeWorld.MAZE_X;
  public static final double CELL_LENGTH = 600 / MazeWorld.MAZE_Y;
  WorldImage boardImage;
  String mode;
  ArrayList<Cell> seen = new ArrayList<Cell>();
  HashMap<Cell, Edge> cameFromEdge = new HashMap<Cell, Edge>();
  boolean startTick = false;
  int place = 0;

  MazeWorld() {
    this.board = this.createBoard(MAZE_X, MAZE_Y);
    this.findMinTree();
    this.boardImage = this.makeBoardImage(MAZE_X, MAZE_Y);
    this.player = new Player(this.board.get(0).get(0));
    this.mode = "";
  }

  // creates the board
  ArrayList<ArrayList<Cell>> createBoard(int x, int y) {
    ArrayList<ArrayList<Cell>> b = new ArrayList<ArrayList<Cell>>(x);
    for (int i = 0; i < x; i++) {
      ArrayList<Cell> row = new ArrayList<Cell>(y);
      for (int j = 0; j < y; j++) {
        row.add(new Cell(i, j));
      }
      b.add(row);
    }
    this.createEdges(b, x, y);
    return b;
  }

  // creates the edges
  void createEdges(ArrayList<ArrayList<Cell>> b, int x, int y) {
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {
        b.get(i).get(j).makeEdges(b);
      }
    }
  }

  // draws the scene
  public WorldScene makeScene() {
    WorldScene w = this.getEmptyScene();
    this.boardImage = this.makeBoardImage(MAZE_X, MAZE_Y);
    w.placeImageXY(this.boardImage, 500, 300);
    if (this.mode.equals("manual")) {
      w.placeImageXY(this.player.drawPlayer(), 
          ((int) ((MazeWorld.CELL_WIDTH) * (this.player.getX() + 0.5))),
          ((int) (MazeWorld.CELL_LENGTH * (this.player.getY() + 0.5))));
    }
    if (this.mode.equals("")) {
      w.placeImageXY(new TextImage("PRESS 'M' TO COMPLETE THE MAZE MANUALLY", 
          32, Color.CYAN), 500, 200);
      w.placeImageXY(new TextImage("PRESS 'B' FOR BREADTH-FIRST SEARCH", 
          32, Color.CYAN), 500, 265);
      w.placeImageXY(new TextImage("PRESS 'D' FOR DEPTH-FIRST SEARCH", 
          32, Color.CYAN), 500, 330);
      w.placeImageXY(new TextImage("PRESS 'R' TO RESTART WITH A NEW MAZE AT ANY TIME", 
          32, Color.CYAN), 500, 400);
    }
    if (this.mode.equals("won")) {
      w.placeImageXY(new TextImage("YOU COMPLETED THE MAZE", 
          32, Color.RED), 500, 250);
      w.placeImageXY(new TextImage("PRESS 'R' TO RESTART WITH A NEW MAZE", 
          32, Color.RED), 500, 350);
    }
    return w;
  }

  // makes the board image
  WorldImage makeBoardImage(int x, int y) {
    WorldImage b = new RectangleImage(0, 0, OutlineMode.SOLID, Color.BLACK);
    for (int i = 0; i < x; i++) {
      WorldImage a = new RectangleImage(0, 0, OutlineMode.SOLID, Color.BLACK);
      for (int j = 0; j < y; j++) {
        a = new AboveImage(a, this.board.get(i).get(j).drawCell(this.board));
      }
      b = new BesideImage(b, a);
    }
    return b;
  }

  // handles key events
  public void onKeyEvent(String k) {
    if (this.mode.equals("")) {
      if (k.equals("m")) {
        this.mode = "manual";
      } else if (k.equals("b")) {
        this.mode = "bfs";
        this.findPath(this.board.get(0).get(0), 
            this.board.get(MazeWorld.MAZE_X - 1).get(MazeWorld.MAZE_Y - 1),
            new Queue<Cell>());
      } else if (k.equals("d")) {
        this.mode = "dfs";
        this.findPath(this.board.get(0).get(0), 
            this.board.get(MazeWorld.MAZE_X - 1).get(MazeWorld.MAZE_Y - 1),
            new Stack<Cell>());
      }
    }
    if (k.equals("r")) {
      this.mode = "";
      this.board = this.createBoard(MAZE_X, MAZE_Y);
      this.findMinTree();
      this.boardImage = this.makeBoardImage(MAZE_X, MAZE_Y);
      this.player = new Player(this.board.get(0).get(0));
      this.startTick = false;
      this.seen = new ArrayList<Cell>();
      this.cameFromEdge = new HashMap<Cell, Edge>();
      this.makeScene();
    } else if (this.mode.equals("manual")) {
      this.player.movePlayer(k, MazeWorld.MAZE_X, MazeWorld.MAZE_Y);
      if (this.player.getX() == MazeWorld.MAZE_X - 1 
          && this.player.getY() == MazeWorld.MAZE_Y - 1) {
        this.mode = "won";
        this.makeScene();
      } else {
        this.makeScene();
      }
    }
  }
  
  // on tick
  public void onTick() {
    if (this.startTick) {
      SeenIterator<Cell> iter = new SeenIterator<Cell>(this.seen);
      if (iter.hasNext()) {
        iter.next().color = Color.CYAN;
      } else {
        this.reconstruct(cameFromEdge, 
            this.board.get(MazeWorld.MAZE_X - 1).get(MazeWorld.MAZE_Y - 1));
        this.mode = "won";
      }
    }
  }

  // finds the minimum spanning tree
  void findMinTree() {
    HashMap<Cell, Cell> representatives = new HashMap<Cell, Cell>();
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>();
    ArrayList<Edge> worklist = this.getEdges();

    for (ArrayList<Cell> ac : this.board) {
      for (Cell c : ac) {
        representatives.put(c, c);
      }
    }

    while (worklist.size() > 0) {
      Edge curr = worklist.remove(0);
      if (this.find(representatives, curr.from).equals(this.find(representatives, curr.to))) {
        place = place + 1;
      } else {
        edgesInTree.add(curr);
        this.union(representatives, this.find(representatives, curr.from), 
            this.find(representatives, curr.to));
      }
    }
    this.removeEdges(edgesInTree);
  }

  // what is x's representative
  Cell find(HashMap<Cell, Cell> reps, Cell x) {
    if (x.equals(reps.get(x))) {
      return x;
    }
    return find(reps, reps.get(x));
  }

  // put x and y together in the same group
  void union(HashMap<Cell, Cell> reps, Cell x, Cell y) {
    reps.put(x, y);
  }

  // gets all the edges in the graph
  ArrayList<Edge> getEdges() {
    ArrayList<Edge> edges = new ArrayList<Edge>();
    for (ArrayList<Cell> cCol : this.board) {
      for (Cell c : cCol) {
        edges.addAll(c.outEdges);
      }
    }
    this.sortEdges(edges);
    return edges;
  }

  // sorts all the edges by weight
  void sortEdges(ArrayList<Edge> edges) {
    Edge current = edges.get(0);
    for (int i = 0; i < edges.size(); i++) {
      current = edges.get(i);
      edges.remove(i);
      this.insert(edges, current);
    }
  }

  // inserts the given edge into the list by weight
  private void insert(ArrayList<Edge> edges, Edge current) {
    for (int i = 0; i < edges.size(); i++) {
      if (edges.get(i).weight >= current.weight) {
        edges.add(i, current);
        return;
      }
    }
    edges.add(current);
  }

  // removes edges for each cell that are not in the tree
  void removeEdges(ArrayList<Edge> edges) {
    for (ArrayList<Cell> ac : this.board) {
      for (Cell c : ac) {
        c.removeEdges(edges);
      }
    }
  }

  // creates the path from the top left to bottom right
  void findPath(Cell from, Cell to, ICollection<Cell> worklist) {
    worklist.add(from);

    while (worklist.size() > 0) {
      Cell curr = worklist.remove();
      if (seen.contains(curr)) {
        place = place + 1;
      } else if (curr.equals(to)) { 
        this.startTick = true;
        return ;
      } else {
        seen.add(curr);
        for (Edge e : curr.outEdges) {
          worklist.add(e.to);
          if (cameFromEdge.containsKey(e.to)) {
            place = place + 1;
          }
          else {
            cameFromEdge.put(e.to, e);
          }
        }
      }
    }
  }

  // reconstruct the path to the source
  void reconstruct(HashMap<Cell, Edge> cameFromEdge, Cell curr) {
    if (curr.equals(this.board.get(0).get(0))) {
      place = place + 1;
    }
    else {
      curr.color = Color.BLUE;
      this.reconstruct(cameFromEdge, cameFromEdge.get(curr).from);
    }
  }
}

class SeenIterator<T> implements Iterator<T> {
  ArrayList<T> items;
  
  SeenIterator(ArrayList<T> items) {
    this.items = items;
  }
  
  // is there another item
  public boolean hasNext() {
    return this.items.size() > 0;
  }
  
  // advances the list
  public T next() {
    T curr = this.items.remove(0);
    return curr;
  }
  
  // remove
  public void remove() {
    throw new UnsupportedOperationException("NO");
  }
}

interface ICollection<T> {
  void add(T t);

  T remove();

  int size();
}

class Stack<T> implements ICollection<T> {
  Deque<T> deque;

  Stack() {
    deque = new Deque<T>();
  }

  // adds the item to the stack
  public void add(T t) {
    deque.addAtHead(t);
  }

  // removes the first item of the stack
  public T remove() {
    return deque.removeFromHead();
  }

  // gets the size of the stack
  public int size() {
    return deque.size;
  }
}

class Queue<T> implements ICollection<T> {
  Deque<T> deque;

  Queue() {
    deque = new Deque<T>();
  }

  // adds the item to the queue
  public void add(T t) {
    deque.addAtTail(t);
  }

  // removes the first item from the queue
  public T remove() {
    return deque.removeFromHead();
  }

  // gets the size of the queue
  public int size() {
    return deque.size;
  }
}

// Represents a boolean-valued question over values of type T
interface IPred<T> {
  boolean apply(T t);
}

class Deque<T> {
  Sentinel<T> header;
  int size;

  /*
   * TEMPLATE FIELDS: ... this.header... -Sentinel<T> ... this.size... -int
   * 
   * METHODS: ... this.addAtHead(T)... -void ... this.addAtTail(T)... -void ...
   * this.removeFromHead()... -T ... this.removeFromTail()... -T ...
   * this.find(IPred<T>)... -ANode<T> ... this.removeNode(ANode<T>)... -void
   */

  // constructor
  Deque() {
    this.header = new Sentinel<T>();
    this.size = 0;
  }

  // constructor that takes a specific header
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // adds a new node with the given data to the head of the deque
  void addAtHead(T t) {
    this.header.addAtHead(t);
    this.size = size + 1;
    this.header.size = size;
  }

  // adds a new node with the given data to the tail of the deque
  void addAtTail(T t) {
    this.header.addAtTail(t);
    this.size = size + 1;
    this.header.size = size;
  }

  // removes the head of the deque
  T removeFromHead() {
    this.size = size - 1;
    this.header.size = size;
    return this.header.removeFromHead();
  }

  // removes the tail of the deque
  T removeFromTail() {
    this.size = size - 1;
    this.header.size = size;
    return this.header.removeFromTail();
  }

  // finds the first node with satisfies the given IPred
  ANode<T> find(IPred<T> pred) {
    return this.header.find(pred);
  }

  // removes the first instance of the given node
  void removeNode(ANode<T> that) {
    this.header.removeNode(that);
  }
}

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // abstract constructor
  ANode(ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("neither next nor prev can be null");
    }
    this.next = next;
    this.prev = prev;
    next.setPrev(this);
    prev.setNext(this);
  }

  ANode() {
    this.next = this;
    this.prev = this;
  }

  // sets the prev
  void setPrev(ANode<T> prev) {
    this.prev = prev;
    prev.next = this;
  }

  // sets the next
  void setNext(ANode<T> next) {
    this.next = next;
    next.prev = this;
  }

  abstract T removeFromHeadHelp(ANode<T> that);

  abstract T removeFromTailHelp(ANode<T> that);

  abstract ANode<T> findHelp(IPred<T> pred);

  abstract boolean sameANode(ANode<T> that);

  abstract boolean sameNode(Node<T> that);

  abstract boolean sameSentinel(Sentinel<T> that);

  abstract void removeHelp(ANode<T> that);
}

class Sentinel<T> extends ANode<T> {
  int size;

  /*
   * TEMPLATE FIELDS: ... this.next... -ANode<T> ... this.prev... -ANode<T>
   * 
   * METHODS: ... this.addToHead(ANode<T>) ... -void ...
   * this.addToTail(ANode<T>) ... -void ... this.removeFromHeadHelp()... -T ...
   * this.removeFromTailHelp()... -T ... this.removeFromHead()... -T ...
   * this.removeFromTail()... -T ... this.find(IPred<T>)... -ANode<T> ...
   * this.findHelp(IPred<T>)... -ANode<T> ... this.removeNode(ANode<T>)... -void
   * ... this.sameANode(ANode<T>)... -boolean ... this.sameNode(Node<T>)...
   * -boolean ... this.sameSentinel(Sentinel<T>)... -boolean ...
   * this.removeHelp(ANode<T>)... -void
   */

  // constructor
  Sentinel() {
    super();
  }

  // adds a node to the head of a deque
  void addAtHead(T t) {
    new Node<T>(t, this.next, this);
  }

  // adds a node to the tail of a deque
  void addAtTail(T t) {
    new Node<T>(t, this, this.prev);
  }

  // removes the node from the head of this deque
  T removeFromHead() {
    return this.next.removeFromHeadHelp(this);
  }

  // helper method for removeFromHead
  T removeFromHeadHelp(ANode<T> that) {
    throw new RuntimeException("Can't remove from an empty list");
  }

  // removes the node from the tail of this deque
  T removeFromTail() {
    return this.prev.removeFromTailHelp(this);
  }

  // helper method for removeFromTail
  T removeFromTailHelp(ANode<T> that) {
    throw new RuntimeException("Can't remove from an empty list");
  }

  // returns the first node that satisfies the given predicate
  ANode<T> find(IPred<T> pred) {
    return this.next.findHelp(pred);
  }

  // helper method for find
  ANode<T> findHelp(IPred<T> pred) {
    return this;
  }

  // finds the node int this deque that matches the given node
  // and removes it from this deque
  void removeNode(ANode<T> that) {
    if (that.sameANode(this)) {
      // this needs to empty, because if the given node to be removed
      // is the sentinel, then this function should do nothing.
    } else {
      this.next.removeHelp(that);
    }
  }

  // returns if two ANodes are equal
  boolean sameANode(ANode<T> that) {
    return that.sameSentinel(this);
  }

  // returns if two ANodes are equal with the given being a node
  boolean sameNode(Node<T> that) {
    return false;
  }

  // returns if two A
  boolean sameSentinel(Sentinel<T> that) {
    return true;
  }

  // helper method for remove
  void removeHelp(ANode<T> that) {
    if (this.size == 0) {
      throw new RuntimeException("Can't remove from an empty list");
    }
  }

}

class Node<T> extends ANode<T> {
  T data;

  /*
   * TEMPLATE FIELDS: ... this.next... -ANode<T> ... this.prev... -ANode<T>
   * 
   * METHODS: ... this.sizeHelp()... -int ... this.addToHead(ANode<T>) ... -void
   * ... this.addToTail(ANode<T>) ... -void ... this.removeFromHeadHelp()... -T
   * ... this.removeFromTailHelp()... -T ... this.findHelp(IPred<T>)...
   * -ANode<T> ... this.removeNode(ANode<T>)... -void ...
   * this.sameANode(ANode<T>)... -boolean ... this.sameNode(Node<T>)... -boolean
   * ... this.sameSentinel(Sentinel<T>)... -boolean ...
   * this.removeHelp(ANode<T>)... -void
   */

  // constructor
  Node(T data) {
    this.next = null;
    this.prev = null;
    this.data = data;
  }

  // constructor
  Node(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    this.data = data;
  }

  // helper method for removeFromHead
  T removeFromHeadHelp(ANode<T> that) {
    that.setNext(this.next);
    return this.data;
  }

  // helper method for removeFromTail
  T removeFromTailHelp(ANode<T> that) {
    that.setPrev(this.prev);
    return this.data;
  }

  // helper function for help
  ANode<T> findHelp(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    } else {
      return this.next.findHelp(pred);
    }
  }

  // returns if two ANodes are the same
  boolean sameANode(ANode<T> that) {
    return that.sameNode(this);
  }

  // returns if two ANodes are the same with the given being a Node
  boolean sameNode(Node<T> that) {
    return (this.data.equals(that.data));
  }

  // returns if two ANodes are the same with the given being a Sentinel
  boolean sameSentinel(Sentinel<T> that) {
    return false;
  }

  // helper function for remove
  void removeHelp(ANode<T> that) {
    if (this.sameANode(that)) {
      this.next.setPrev(this.prev);
      this.prev.setNext(this.next);
    } else {
      this.next.removeHelp(that);
    }
  }
}

class ExamplesMaze {

  MazeWorld g;

  // to change the size change the MAZE_X and MAZE_Y
  // constants at the top of the MazeWorld class
  void testGame(Tester t) {
    g = new MazeWorld();
    g.bigBang(1000, 600, 0.001);
  }

  void testSortEdges(Tester t) {
    Cell c1 = new Cell(0, 0);
    Cell c2 = new Cell(1, 1);
    ArrayList<Edge> testEdges = new ArrayList<Edge>();
    for (int i = 0; i < 100; i++) {
      testEdges.add(new Edge(c1, c2));
    }
    g = new MazeWorld();
    g.sortEdges(testEdges);
    for (int i = 0; i < 99; i++) {
      t.checkExpect(testEdges.get(i).weight <= testEdges.get(i + 1).weight, true);
    }
  }

  void testCellEquals(Tester t) {
    Cell cell1 = new Cell(1, 1);
    Cell cell2 = new Cell(1, 1);
    Cell cell3 = new Cell(0, 0);
    Cell cell4 = new Cell(1, 0);
    Cell cell5 = new Cell(0, 1);
    t.checkExpect(cell1.equals(cell2), true);
    t.checkExpect(cell1.equals(cell3), false);
    t.checkExpect(cell1.equals(cell4), false);
    t.checkExpect(cell1.equals(cell5), false);
    t.checkExpect(cell1.equals(4), false);
    t.checkExpect(cell1.equals("red"), false);
  }
}