package com.dahutu.robinhood;

import java.security.Provider;
import java.util.*;
import java.util.stream.Collectors;

/*
新鲜罗兵侠店面，新题，用topological sort算一个service mesh的loadfactor

给一堆service call relations和一个entrypoint，比如 [a:b,c, b:c] a意思就是a会call b和c，b还会call c，
然后assume entrypoint a有一个request，这样a的load就是1，b也是1，c是2。就是每个service都会completely fanout它收到的requests，
比如如果c要有downstream的话，c给他每个downstream的load就是2.

用topological sort做，要注意除了entrypoint之外还有别的service可能没有indegree，
这些service 的load要是0，不然漏掉的话graph traverse不完

还要注意有些service的downstream是不存在的，ignore就好
*/


public class ServiceLoad {
  public static void main(String[] args) {
    String[] services = {"a=b,c,d", "b=e", "c=e", "d=b,e","e=f"};
    ServiceLoad s = new ServiceLoad();
    String[] serviceLoads = s.findLoadFactor(services, "a");
    System.out.println(Arrays.asList(serviceLoads));
  }

  public String[] findLoadFactor(String[] services, String entrypoint) {
    Map<String, Set<String>> dependencyMap = populateRelationMap(services);

    Map<String, Integer> loadMap = new HashMap<>();
    dfs(dependencyMap, loadMap, entrypoint);

    List<String> results = new ArrayList<>();
    for (String service : loadMap.keySet()) {
        results.add(String.format("%s*%d", service, service.equals(entrypoint) ? 1 : loadMap.get(service)));
    }
    Collections.sort(results);

    String[] temp = new String[results.size()];
    return results.toArray(temp);
  }

  Map<String, Set<String>> populateRelationMap(String[] services) {
    Map<String, Set<String>> map = new HashMap<>();
    for (String service : services) {
      String[] parts = service.split("=", 2);
      String caller = parts[0];
      if (parts[1] == null || parts[1].isEmpty()) {
        map.put(caller, Collections.EMPTY_SET);
      } else {
        String[] callee = parts[1].split(",");
        Set<String> value = new HashSet<>(Arrays.asList(callee));
        map.put(caller, value);
      }
    }

    return map;
  }

  private void dfs(Map<String, Set<String>> relationMap, Map<String, Integer> loadMap, String entrypoint) {
    loadMap.put(entrypoint, loadMap.getOrDefault(entrypoint, 0) + 1);
    if (relationMap.containsKey(entrypoint)) {
      for (String next : relationMap.get(entrypoint)) {
        dfs(relationMap, loadMap, next);
      }
    }
  }
}
