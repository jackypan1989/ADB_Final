package ntu.im.course.adb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.Parameter;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.Response;
import com.aetrion.flickr.Transport;
import com.aetrion.flickr.auth.AuthUtilities;
import com.aetrion.flickr.people.PeopleInterface;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.GeoData;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotoUtils;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.aetrion.flickr.photos.geo.GeoInterface;
import com.aetrion.flickr.places.Location;
import com.aetrion.flickr.places.Place;
import com.aetrion.flickr.places.PlacesInterface;
import com.aetrion.flickr.places.PlacesList;
import com.aetrion.flickr.util.IOUtilities;
import com.aetrion.flickr.util.XMLUtilities;

public class DataCollector {
	private static Flickr flickr;
	private PlacesInterface places_interface;
	private GeoInterface geo_interface;
	private PhotosInterface photo_interface;
	private PeopleInterface people_interface;

	public static final String apiKey = "39e0025b08410f5c4108ec8057879d60";
	public static final String sharedSecret = "f917f19d60b8316b";
	public static Transport transport;
	public static final String METHOD_GET_INFO = "flickr.photos.getInfo";

	public DataCollector() {
		try {
			transport = new REST();
			flickr = new Flickr(apiKey, sharedSecret, transport);
			places_interface = flickr.getPlacesInterface();
			photo_interface = flickr.getPhotosInterface();
			geo_interface = flickr.getGeoInterface();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Element apiPlacesGetInfo(String woe_id) throws IOException,
			SAXException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", "flickr.places.getInfo"));
		parameters.add(new Parameter("api_key", apiKey));
		parameters.add(new Parameter("woe_id", woe_id));

		Response response = transport.get(transport.getPath(), parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		Element placeElement = (Element) response.getPayload();
		return placeElement;
	}

	public Element apiPhotosGetInfo(String photo_id) throws IOException,
			SAXException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", "flickr.photos.getInfo"));
		parameters.add(new Parameter("api_key", apiKey));
		parameters.add(new Parameter("photo_id", photo_id));

		Response response = transport.get(transport.getPath(), parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		Element placeElement = (Element) response.getPayload();
		return placeElement;
	}

	public Element apiPeopleGetInfo(String user_id) throws IOException,
			SAXException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", "flickr.people.getInfo"));
		parameters.add(new Parameter("api_key", apiKey));
		parameters.add(new Parameter("user_id", user_id));

		Response response = transport.get(transport.getPath(), parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		Element placeElement = (Element) response.getPayload();
		return placeElement;
	}

	public Element apiPhotosSearch(String woe_id) throws IOException,
			SAXException, FlickrException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("method", "flickr.photos.search"));
		parameters.add(new Parameter("api_key", apiKey));
		parameters.add(new Parameter("woe_id", woe_id));
		parameters.add(new Parameter("per_page", 500));

		Response response = transport.get(transport.getPath(), parameters);
		if (response.isError()) {
			throw new FlickrException(response.getErrorCode(),
					response.getErrorMessage());
		}
		Element photosElement = (Element) response.getPayload();
		return photosElement;
	}

	private Location old_parseLocation(Element locationElement) {
		Location location = new Location();
		Element localityElement = (Element) locationElement
				.getElementsByTagName("locality").item(0);
		Element countyElement = (Element) locationElement.getElementsByTagName(
				"county").item(0);
		Element regionElement = (Element) locationElement.getElementsByTagName(
				"region").item(0);
		Element countryElement = (Element) locationElement
				.getElementsByTagName("country").item(0);

		location.setPlaceId(locationElement.getAttribute("place_id"));
		// location.setName(locationElement.getAttribute("name"));
		location.setPlaceUrl(locationElement.getAttribute("place_url"));
		location.setWoeId(locationElement.getAttribute("woeid"));
		location.setLatitude(locationElement.getAttribute("latitude"));
		location.setLongitude(locationElement.getAttribute("longitude"));
		location.setPlaceType(determineType(locationElement
				.getAttribute("place_type")));

		location.setLocality(parseLocationPlace(localityElement,
				Place.TYPE_LOCALITY));
		location.setCounty(parseLocationPlace(countyElement, Place.TYPE_COUNTY));
		location.setRegion(parseLocationPlace(regionElement, Place.TYPE_REGION));
		location.setCountry(parseLocationPlace(countryElement,
				Place.TYPE_COUNTRY));
		return location;
	}

	private String parseLocation(Element locationElement) {
		Node locationNode = locationElement.getElementsByTagName("location")
				.item(0);
		Node neighbourhoodNode = locationElement.getElementsByTagName(
				"neighbourhood").item(0);
		Node localityNode = locationElement.getElementsByTagName("locality")
				.item(0);
		Node countyNode = locationElement.getElementsByTagName("county")
				.item(0);
		Node regionNode = locationElement.getElementsByTagName("region")
				.item(0);
		Node countryNode = locationElement.getElementsByTagName("country")
				.item(0);

		String county = null;
		if (countyNode != null)
			county = countyNode.getTextContent();

		String region = null;
		if (regionNode != null)
			region = regionNode.getTextContent();

		String locality = null;
		if (localityNode != null)
			locality = localityNode.getTextContent();

		// String country = countryNode.getTextContent();

		String location_name = "";

		// if (country != null)
		// location_name = country;

		if (region != null)
			location_name = region;
		// location_name = region + "," + location_name;

		if (county != null)
			location_name = county + "," + location_name;

		if (locality != null)
			location_name = locality + "," + location_name;

		return location_name;

	}

	private Place parseLocationPlace(Element element, int type) {
		Place place = new Place();
		place.setName(XMLUtilities.getValue(element));
		place.setPlaceId(element.getAttribute("place_id"));
		place.setPlaceUrl(element.getAttribute("place_url"));
		place.setWoeId(element.getAttribute("woeid"));
		place.setLatitude(element.getAttribute("latitude"));
		place.setLongitude(element.getAttribute("longitude"));
		place.setPlaceType(type);
		return place;
	}

	private int determineType(String typeString) {
		int placeType = 0;
		if (typeString.equals("locality")) {
			placeType = Place.TYPE_LOCALITY;
		} else if (typeString.equals("county")) {
			placeType = Place.TYPE_COUNTY;
		} else if (typeString.equals("region")) {
			placeType = Place.TYPE_REGION;
		} else if (typeString.equals("country")) {
			placeType = Place.TYPE_COUNTRY;
		}
		return placeType;
	}

	public void run() throws FlickrException, IOException, SAXException {
		String woe_id = "23424977"; // usa
		// Element placeElement = apiPlacesGetInfo(woe_id);

		SearchParameters search_parameters = new SearchParameters();
		search_parameters.setWoeId(woe_id);
		search_parameters.setText("travel");

		PhotoList list = photo_interface.search(search_parameters, 250, 1);
		System.out.println("total photos in America:" + list.getTotal());
		System.out.println("page_index:" + list.getPage());
		System.out.println("page_size:" + list.getPages());
		System.out.println("photo_per_page:" + list.getPerPage());
		System.out.println("");

		Set<String> set = findLocationSet(woe_id);

		BufferedWriter bufWriter = new BufferedWriter(new FileWriter(
				"location_travel_group3000.txt"));

		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			bufWriter.write(iterator.next());
			bufWriter.newLine();
		}

		bufWriter.close();
	}

	public Set<String> findLocationSet(String woe_id) {
		SearchParameters search_parameters = new SearchParameters();
		search_parameters.setWoeId(woe_id);
		//search_parameters.setText("travel");
		search_parameters.setGroupId("74744754@N00");
		
		Set<String> set = new HashSet<String>();

		for (int i = 1; i <= 600000; i++) {
			PhotoList photo_list = null;

			try {
				photo_list = photo_interface.search(search_parameters, 250, i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FlickrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int j = 0; j <= 250; j++) {
				try {
					Photo photo = (Photo) photo_list.get(j);
					System.out.println("page_index:" + i + " photo:" + j);
					String photo_id = photo.getId();
					System.out.println("photo_id:" + photo_id);
					String owner_id = (photo.getOwner()).getId();
					System.out.println("owner_id:" + owner_id);
					Element photoElement = apiPhotosGetInfo(photo_id);
					String location = parseLocation(photoElement);
					System.out.println("location:" + location);
					String str = owner_id + "," + location;
					set.add(str);
					System.out.println("collected count:" + set.size());
					System.out.println("");
					if (set.size() >= 3000)
						return set;
				} catch (FlickrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		}

		return set;
	}

	public Set<String> findUserSet(String woe_id) {
		SearchParameters search_parameters = new SearchParameters();
		search_parameters.setWoeId(woe_id);
		search_parameters.setText("travel");

		Set<String> user_set = new HashSet<String>();

		// 2706 pages about "travel page"
		for (int i = 1; i <= 2700; i++) {
			PhotoList photo_list = null;
			try {
				photo_list = photo_interface.search(search_parameters, 250, i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FlickrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int j = 0; j < 250; j++) {
				try {
					Photo photo = (Photo) photo_list.get(j);
					//System.out.println("page_index:" + i + " photo:" + j);
					String photo_id = photo.getId();
					//System.out.println("photo_id:" + photo_id);
					String owner_id = (photo.getOwner()).getId();
					//System.out.println("owner_id:" + owner_id);
					
					Element userElement = apiPeopleGetInfo(owner_id);
					Element photosElement = XMLUtilities.getChild(userElement, "photos");
					int count = Integer.parseInt(XMLUtilities.getChildValue(photosElement, "count"));
					
					if (count <= 500)
						user_set.add(owner_id);
					    System.out.println("added_user:" + user_set.size());
					if (user_set.size() >= 500)
						return user_set;
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FlickrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return user_set;
	}

	public Set<String> findSet(String woe_id) {
		SearchParameters search_parameters = new SearchParameters();
		search_parameters.setWoeId(woe_id);

		Set<String> user_set = findUserSet(woe_id);
		Set<String> result = new HashSet<String>();
		PhotoList photo_list = null;

		System.out.println("total user:" + user_set.size());
		int user_count = 0;
		String str = "";
		int same_str = 0;

		for (String owner_id : user_set) {
			search_parameters.setUserId(owner_id);

			try {
				photo_list = photo_interface.search(search_parameters, 250, 1);
				int pages = photo_list.getPages();
				for (int i = 1; i <= pages; i++) {
					photo_list = photo_interface.search(search_parameters, 250,
							i);
					for (Object o : photo_list) {
						Photo photo = (Photo) o;
						String photo_id = photo.getId();
						Element photoElement = apiPhotosGetInfo(photo_id);
						String location = parseLocation(photoElement);
						System.out.println("location:" + location);
						if (str.equals(owner_id + "," + location))
							same_str++;
						if (same_str >= 30) {
							same_str = 0;
							continue;
						}
						str = owner_id + "," + location;
						result.add(str);
						System.out.println("collected count:" + result.size());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FlickrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			user_count++;
			System.out.println("done user:" + user_count);
		}

		return result;
	}

	public static void main(String[] args) {
		try {
			DataCollector dc = new DataCollector();
			dc.run();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
