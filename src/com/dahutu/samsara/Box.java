package com.dahutu.samsara;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Box {
    Set<Valuable> valuables;
    Optional<Integer> size;

    public Box() {
        this.valuables = new HashSet<>();
        size = Optional.empty();
    }

    public Box(int size) {
        super();
        this.size = Optional.of(size);
    }

    public void add(Valuable valuable) throws Exception {
      if (!valuables.contains(valuable)) {
        if (size.isEmpty() || valuables.size() < size.get()) {
          valuables.add(valuable);
        } else {
          throw new Exception("Box size reached. Valuable not added!");
        }
      } else {
        throw new Exception("Valuable already exists in box.  Valuable not added!");
      }
    }

    public void remove(Valuable valuable) {
        valuables.remove(valuable);
    }
}
