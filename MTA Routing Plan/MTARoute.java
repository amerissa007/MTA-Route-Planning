import java.util.*;
import java.io.*;

public class MTARoute {
	private Map<String, List<Edge>> adjacencyList;

	private static class Edge {
		String toStationName;
		String toStationName2;
		//		double longitude;
		//		double latitude;
		String subwayLine;

		public Edge(String toStationName, String toStationName2, String subwayLine) {
			this.toStationName = toStationName;
			this.toStationName2 = toStationName2;
			//			this.longitude = longitude;
			//			this.latitude = latitude;
			this.subwayLine = subwayLine;
		}
	}

	public void SubwaySystem(String filename) throws IOException {
		adjacencyList = new HashMap<>();

		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\AmerI\\eclipse-workspace\\Assignment 4.1\\src\\mta_stations.csv"));
		br.readLine();
		String line=null;
		HashMap<String, HashSet<String>>subwayConnections = new HashMap<>();
		while ((line = br.readLine()) != null) {
			if (line.length() == 0) {
				continue;
			}

			String[] fields = line.split(",");
			String stationId = fields[1];
			String stationName = fields[2];
			String longLat = fields[3];
			//			longLat = longLat.substring(7,longLat.length()-1);
			//			int whiteSpace = longLat.indexOf(" ");
			//			double longitude = Double.parseDouble(longLat.substring(0,whiteSpace));
			//			double latitude = Double.parseDouble(longLat.substring(whiteSpace,longLat.length()-1));			
			String[] subwayLines = fields[4].split("-");
			HashSet<String> lines = new HashSet<>();
			for(String random: subwayLines) {
				lines.add(random);
			}
			if(subwayConnections.containsKey(stationName)) {
				for(String l: lines) {
					subwayConnections.get(stationName).add(l);

				}
			}else {
				subwayConnections.put(stationName, lines);

			}


			//	subwayConnections.put(stationName, lines);

			//			for (String subwayLine : subwayLines) {
			//				addStation(stationId, stationName, subwayLine);
			//			}

		}
		for(String station1: subwayConnections.keySet()) {
			HashSet<String> strings = subwayConnections.get(station1);

			for(String station2: subwayConnections.keySet()) {
				HashSet<String> strings1 = subwayConnections.get(station2);

				for(String line1: strings) {

					if(strings1.contains(line1)&& !station1.equals(station2)) {
						//							if(station1.equals("Canal St")){
						//							System.out.println(station1 + "," + station2 + "," + line1);
						addStation(station1, station2, line1);

					}

				}
			}
		}

		//		for (Edge edge : adjacencyList.get("Astor Pl")) {
		//			System.out.println(edge.toStationName2);
		//		}

		br.close();
	}


	private void addStation(String stationName,String stationName2, String subwayLine) {
		List<Edge> edges = adjacencyList.getOrDefault(stationName, new ArrayList<>());
		//		for (Edge edge : edges) {
		//			if (edge.subwayLine.equals(subwayLine)) {
		//				return;  
		//			}
		//		}

		edges.add(new Edge(stationName, stationName2, subwayLine));
		adjacencyList.put(stationName, edges);
	}

	public List<String> bfs(String startStation, String endStation) {
		Map<String, String> prev = new HashMap<>();
		Map<String, String> subwayLineAtPrevStation = new HashMap<>();
		Set<String> visited = new HashSet<>();

		Queue<String> queue = new LinkedList<>();
		queue.add(startStation);
		//		for (Edge edge : adjacencyList.get(stack.pop())) {
		//			System.out.println(edge.toStationName2);
		//		}
		ArrayList<String> line = new ArrayList<>();
		//	line.add(startStation + "(" + );

		while (!queue.isEmpty()) {

			String station = queue.poll();
			//		System.out.println(adjacencyList.get(station));
			if (station.contains(endStation)) {

				//	System.out.println("Hello");
				break;
			}
			visited.add(station);

			//line.clear();

			for (Edge edge : adjacencyList.get(station)) {
				String toStation = edge.toStationName2;
				//				if(toStation.equals("Canal St")) {
				//					System.out.print("YAY");
				//				}
				//		System.out.println(toStation);
				String subwayLine = edge.subwayLine;
				//				if(!line.contains(station)) {
				//					line.add(station + "(" + subwayLine + ")");
				//				}
				//	System.out.println(toStation);
				if (!visited.contains(toStation)) {
					//	visited.add(toStation);
					prev.put(toStation,station);
					//				subwayLineAtPrevStation.put(toStation + "-" + subwayLine, subwayLine); //method doesn't add elements to HashMap
					subwayLineAtPrevStation.put(toStation, subwayLine);
					//			System.out.print(toStation);
					//			line.add(toStation);
					queue.add(toStation);
				}
			}
		}
		//	System.out.println(line);

		List<String> path = new ArrayList<>();
		String station = endStation;

		while (prev.containsKey(station)) {
			path.add(0, station);
			station = prev.get(station);
		}
		path.add(0, station);
		//	System.out.print(path.get(0));
		String currentLine = subwayLineAtPrevStation.get(path.get(0));
		String currentTransferStation = " "; 

		for (int i = 1; i < path.size(); i++) {
			String stationName = path.get(i);
			List<Edge> edges = adjacencyList.get(stationName);
			boolean transferRequired = true;
			double weight = Double.MAX_VALUE; 

			//		        for (Edge edge : edges) {
			//		            if (edge.subwayLine.equals(currentLine)) {
			//		                double lat1 = edge.latitude;
			//		                double lon1 = edge.longitude;
			//		                double lat2 = edge.latitude;
			//		                double lon2 = edge.longitude;
			//		                double dist = Math.sqrt(Math.pow(lat2-lat1, 2) + Math.pow(lon2-lon1, 2));
			//		                if (dist < weight) {
			//		                    weight = dist;
			//		                }
			//		                transferRequired = false;
			//		                break;
			//		            }
			//		        }

			if (transferRequired) {
				line.add(stationName + " (" +subwayLineAtPrevStation.get(stationName) + ")");
				//				path.add(i, "Transfer at " + currentTransferStation + " to " + currentLine + " line");
				//		//		System.out.println(stationName);
				//				currentLine = subwayLineAtPrevStation.get(stationName);
				//				currentTransferStation = path.get(i);
				i++;
			} else {
				path.add(i, "Take the " + currentLine + " to " + stationName + " (" + String.format("%.2f", weight) + " km)");
			}
		}
		line.add(endStation);

		//	path.add("Take the " + currentLine + " to " + endStation);
		return line;
	}

	public static void main(String[] args) {

		try {
			MTARoute subway = new MTARoute();
			subway.SubwaySystem("C:\\Users\\AmerI\\eclipse-workspace\\Assignment 4.1\\src\\mta_stations.csv");
			Scanner scan = new Scanner(System.in);
			System.out.print("Enter start station: ");
			String startStation = scan.nextLine();
			System.out.print("Enter end station: ");
			String endStation = scan.nextLine();
			System.out.println("Your Next Stops Are:");
			List<String> shortestPath = subway.bfs(startStation, endStation);
			System.out.println(shortestPath);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error reading file: " + e.getMessage());
		}
	}

}