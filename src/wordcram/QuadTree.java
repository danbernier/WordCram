package wordcram;

import java.util.ArrayList;
import java.awt.geom.Rectangle2D;

class QuadTree<T extends HasRectangle> {
  QuadTree<T> kidA;
  QuadTree<T> kidB;
  QuadTree<T> kidC;
  QuadTree<T> kidD;
  ArrayList<T> objects;

  double x;
  double y;
  double width;
  double height;
  Rectangle2D rectangle;

  public QuadTree(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.rectangle = new Rectangle2D.Double(x, y, width, height);

    this.objects = new ArrayList<T>();

    if (width > 30 && height > 30) {
      double halfW = (width * 0.5);
      double halfH = (height * 0.5);
      kidA = new QuadTree<T>(x        , y        , halfW, halfH);  // top right
      kidB = new QuadTree<T>(x + halfW, y        , halfW, halfH);  // top left
      kidC = new QuadTree<T>(x        , y + halfH, halfW, halfH);  // bottom right
      kidD = new QuadTree<T>(x + halfW, y + halfH, halfW, halfH);  // bottom left
    }
  }

  void add(T object) {
    Rectangle2D rect = object.getRectangle();
    if (this.contains(rect)) {
      if (kidA != null && kidA.contains(rect)) {
        kidA.add(object);
        return;
      }
      if (kidB != null && kidB.contains(rect)) {
        kidB.add(object);
        return;
      }
      if (kidC != null && kidC.contains(rect)) {
        kidC.add(object);
        return;
      }
      if (kidD != null && kidD.contains(rect)) {
        kidD.add(object);
        return;
      }

      // else, if no kid added it...
      this.objects.add(object);
    }
  }

  T findFirstMatch(T object, Predicate<T> predicate) {
    Rectangle2D rect = object.getRectangle();

    if (contains(rect) || intersects(rect)) {

      for (T anObject : objects) {
        if (predicate.matches(anObject)) {
          return anObject;
        }
      }

      if (kidA != null) {
        T fromKid = kidA.findFirstMatch(object, predicate);
        if (fromKid != null) {
          return fromKid;
        }
      }
      if (kidB != null) {
        T fromKid = kidB.findFirstMatch(object, predicate);
        if (fromKid != null) {
          return fromKid;
        }
      }
      if (kidC != null) {
        T fromKid = kidC.findFirstMatch(object, predicate);
        if (fromKid != null) {
          return fromKid;
        }
      }
      if (kidD != null) {
        T fromKid = kidD.findFirstMatch(object, predicate);
        if (fromKid != null) {
          return fromKid;
        }
      }
    }

    return null;
  }

  private boolean intersects(Rectangle2D rect) {
    return this.rectangle.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
  }
  private boolean contains(Rectangle2D rect) {
    return this.rectangle.contains(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
  }
}
