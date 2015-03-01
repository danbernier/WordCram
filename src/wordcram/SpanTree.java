package wordcram;

import java.util.ArrayList;
import java.awt.geom.Rectangle2D;

class SpanTree<T extends HasRectangle> {
  SpanTree<T> kidA;
  SpanTree<T> kidB;
  ArrayList<T> objects;

  double x;
  double y;
  double width;
  double height;
  Rectangle2D rectangle;

  public SpanTree(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.rectangle = new Rectangle2D.Double(x, y, width, height);

    this.objects = new ArrayList<T>();

    if (width > 30 && height > 30) {
      if (width > height) {
        double halfW = (width * 0.5);
        kidA = new SpanTree<T>(x, y, halfW, height);
        kidB = new SpanTree<T>(x + halfW, y, halfW, height);
      } else {
        double halfH = (height * 0.5);
        kidA = new SpanTree<T>(x, y, width, halfH);
        kidB = new SpanTree<T>(x, y + halfH, width, halfH);
      }
    }
  }

  void add(T object) {
    Rectangle2D rect = object.getRectangle();
    if (this.contains(rect)) {
      if (kidA != null && kidA.contains(rect)) {
        kidA.add(object);
      } else if (kidB != null && kidB.contains(rect)) {
        kidB.add(object);
      } else {
        this.objects.add(object);
      }
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
        T fromKids = kidA.findFirstMatch(object, predicate);
        if (fromKids != null) {
          return fromKids;
        }
        else if (kidB != null) {
          return kidB.findFirstMatch(object, predicate);
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
