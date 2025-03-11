# AgriGrow Android App - Class Diagram

## Architecture Overview

The AgriGrow app follows the MVVM (Model-View-ViewModel) architecture pattern, which provides a clean separation of concerns:

- **Model**: Data classes and repositories that handle data operations
- **View**: Activities and Fragments that display UI to the user
- **ViewModel**: Classes that manage UI-related data in a lifecycle-conscious way

## Class Diagram

```
+---------------------+     +------------------------+     +----------------------+
|     Activities      |     |       Fragments        |     |     ViewModels       |
+---------------------+     +------------------------+     +----------------------+
| MainActivity        |<--->| HomeFragment           |<--->| HomeViewModel        |
| HomeActivity        |     | PlantsFragment         |<--->| PlantsViewModel      |
| SplashActivity      |     | GuidesFragment         |<--->| GuidesViewModel      |
|                     |     | ForumFragment          |<--->| ForumViewModel       |
|                     |     | ProfileFragment        |<--->| ProfileViewModel     |
+---------------------+     +------------------------+     +----------------------+
         |                            |                             |
         v                            v                             v
+---------------------+     +------------------------+     +----------------------+
|     Adapters        |     |        Models          |     |     Repositories     |
+---------------------+     +------------------------+     +----------------------+
| PlantAdapter        |---->| Plant                  |<----| PlantRepository      |
| GardeningGuideAdapter|---->| GardeningTechnique     |<----| GuideRepository      |
| ForumPostAdapter    |---->| ForumPost              |<----| ForumRepository      |
|                     |     | User                   |<----| UserRepository       |
|                     |     | GardenTip              |     |                      |
+---------------------+     +------------------------+     +----------------------+
                                      |                             |
                                      v                             v
                             +------------------------+     +----------------------+
                             |         DAOs           |     |     Utilities        |
                             +------------------------+     +----------------------+
                             | PlantDao               |     | LocationHelper       |
                             | GardeningTechniqueDao  |     | WeatherHelper        |
                             | ForumPostDao           |     | PlantingScheduleHelper|
                             |                        |     | ImageUtils           |
                             |                        |     | ARUtils              |
                             +------------------------+     +----------------------+
                                      |                             |
                                      v                             v
                             +------------------------+     +----------------------+
                             |      Database          |     |   External Services  |
                             +------------------------+     +----------------------+
                             | DatabaseHelper         |---->| WeatherAPI           |
                             | AppDatabase            |     | ARCore               |
                             |                        |     | Maps API             |
                             |                        |     | Image Recognition API|
                             +------------------------+     +----------------------+
```

## Detailed Class Descriptions

### Activities

#### MainActivity
- Entry point for the application
- Implements splash screen functionality
- Redirects to HomeActivity after initialization

```java
public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Use a handler to delay loading the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
```

#### HomeActivity
- Contains bottom navigation
- Hosts different fragments
- Manages navigation between fragments
- Handles permission requests

```java
public class HomeActivity extends AppCompatActivity {
    private LocationHelper locationHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // Initialize location helper
        locationHelper = new LocationHelper(this);
        
        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_plants:
                    selectedFragment = new PlantsFragment();
                    break;
                case R.id.navigation_guides:
                    selectedFragment = new GuidesFragment();
                    break;
                case R.id.navigation_forum:
                    selectedFragment = new ForumFragment();
                    break;
                case R.id.navigation_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            
            return false;
        });
        
        // Load default fragment
        loadFragment(new HomeFragment());
    }
    
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
```

### Fragments

#### HomeFragment
- Displays dashboard with garden summary
- Shows weather information
- Shows task reminders
- Provides quick access to common actions

```java
public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        // Observe weather data
        viewModel.getWeatherData().observe(getViewLifecycleOwner(), weatherData -> {
            binding.textViewTemperature.setText(weatherData.getTemperature() + "°C");
            binding.textViewWeatherCondition.setText(weatherData.getCondition());
            binding.imageViewWeatherIcon.setImageResource(getWeatherIcon(weatherData.getCondition()));
        });
        
        // Observe garden summary
        viewModel.getGardenSummary().observe(getViewLifecycleOwner(), summary -> {
            binding.textViewPlantCount.setText(summary.getPlantCount() + " Plants");
            binding.textViewNextTask.setText("Next: " + summary.getNextTask());
        });
        
        // Load tasks for today
        viewModel.getTodayTasks().observe(getViewLifecycleOwner(), tasks -> {
            // Populate task list
        });
    }
    
    private int getWeatherIcon(String condition) {
        // Return appropriate weather icon based on condition
        return R.drawable.ic_sun; // Default
    }
}
```

#### PlantsFragment
- Shows browsable catalog of plants
- Allows filtering and searching
- Displays plant categories
- Navigates to plant details on click

#### GuidesFragment
- Displays gardening technique guides
- Organizes guides by categories
- Shows featured and recently added guides
- Provides search functionality

#### ForumFragment
- Shows community posts
- Allows filtering by categories
- Provides ability to create new posts
- Handles likes and comments

#### ProfileFragment
- Displays user information
- Shows garden statistics and achievements
- Lists plants in user's garden
- Shows activity history

### Models

#### Plant
- Represents a plant in the database
- Contains all plant information and properties
- Manages bookmarking state

```java
@Entity(tableName = "plants")
public class Plant {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "name")
    private String name;
    
    @ColumnInfo(name = "scientific_name")
    private String scientificName;
    
    @ColumnInfo(name = "description")
    private String description;
    
    @ColumnInfo(name = "image_url")
    private String imageUrl;
    
    @ColumnInfo(name = "watering_frequency")
    private int wateringFrequency;
    
    @ColumnInfo(name = "sunlight_requirement")
    private String sunlightRequirement;
    
    @ColumnInfo(name = "difficulty_level")
    private int difficultyLevel;
    
    @ColumnInfo(name = "is_bookmarked")
    private boolean isBookmarked;
    
    // Additional properties, constructors, getters, and setters
}
```

#### User
- Stores user profile information
- Tracks gardening level and achievements
- Manages user preferences

#### ForumPost
- Represents a post in the community forum
- Stores post content, author, and timestamps
- Tracks comments and likes

#### GardeningTechnique
- Contains step-by-step gardening guides
- Includes difficulty level and requirements
- Stores related images and videos

### ViewModels

#### HomeViewModel
- Manages data for the home dashboard
- Fetches weather information
- Loads garden summary and task reminders

```java
public class HomeViewModel extends ViewModel {
    private final WeatherHelper weatherHelper;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    
    private final MutableLiveData<WeatherData> weatherData = new MutableLiveData<>();
    private final MutableLiveData<GardenSummary> gardenSummary = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> todayTasks = new MutableLiveData<>();
    
    public HomeViewModel() {
        weatherHelper = new WeatherHelper();
        plantRepository = new PlantRepository();
        userRepository = new UserRepository();
        
        loadWeatherData();
        loadGardenSummary();
        loadTodayTasks();
    }
    
    private void loadWeatherData() {
        // Fetch weather data from API
        weatherHelper.getCurrentWeather(new WeatherCallback() {
            @Override
            public void onWeatherLoaded(WeatherData data) {
                weatherData.postValue(data);
            }
            
            @Override
            public void onError(Exception e) {
                // Handle error
            }
        });
    }
    
    private void loadGardenSummary() {
        // Load garden summary from repository
    }
    
    private void loadTodayTasks() {
        // Load today's tasks from repository
    }
    
    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }
    
    public LiveData<GardenSummary> getGardenSummary() {
        return gardenSummary;
    }
    
    public LiveData<List<Task>> getTodayTasks() {
        return todayTasks;
    }
}
```

#### PlantsViewModel
- Manages plant catalog data
- Handles filtering and searching
- Manages bookmarking functionality

#### GuidesViewModel
- Manages gardening guides data
- Handles guide categories and filtering
- Tracks recently viewed guides

#### ForumViewModel
- Manages forum post data
- Handles post creation and interaction
- Manages filtering and sorting

#### ProfileViewModel
- Manages user profile data
- Tracks achievements and statistics
- Handles garden management

### Repositories

#### PlantRepository
- Acts as single source of truth for plant data
- Fetches data from local database or remote API
- Caches data for offline access

```java
public class PlantRepository {
    private final PlantDao plantDao;
    private final AppExecutors executors;
    
    public PlantRepository() {
        AppDatabase db = AppDatabase.getInstance();
        plantDao = db.plantDao();
        executors = AppExecutors.getInstance();
    }
    
    public LiveData<List<Plant>> getAllPlants() {
        return plantDao.getAllPlants();
    }
    
    public LiveData<List<Plant>> getPlantsByCategory(String category) {
        return plantDao.getPlantsByCategory(category);
    }
    
    public LiveData<Plant> getPlantById(int id) {
        return plantDao.getPlantById(id);
    }
    
    public void insertPlant(Plant plant) {
        executors.diskIO().execute(() -> plantDao.insert(plant));
    }
    
    public void updatePlant(Plant plant) {
        executors.diskIO().execute(() -> plantDao.update(plant));
    }
    
    public void toggleBookmark(int plantId, boolean isBookmarked) {
        executors.diskIO().execute(() -> plantDao.updateBookmarkStatus(plantId, isBookmarked));
    }
}
```

#### GuideRepository
- Provides access to gardening guides
- Handles guide filtering and sorting
- Manages guide categories

#### ForumRepository
- Manages forum post data
- Handles post creation and interaction
- Syncs with remote database

#### UserRepository
- Manages user data and preferences
- Handles authentication and profile updates
- Tracks achievements and statistics

### DAOs (Data Access Objects)

#### PlantDao
- Provides database operations for plants
- Defines queries for filtering and sorting
- Handles plant CRUD operations

```java
@Dao
public interface PlantDao {
    @Query("SELECT * FROM plants")
    LiveData<List<Plant>> getAllPlants();
    
    @Query("SELECT * FROM plants WHERE id = :id")
    LiveData<Plant> getPlantById(int id);
    
    @Query("SELECT * FROM plants WHERE category = :category")
    LiveData<List<Plant>> getPlantsByCategory(String category);
    
    @Query("SELECT * FROM plants WHERE is_bookmarked = 1")
    LiveData<List<Plant>> getBookmarkedPlants();
    
    @Insert
    void insert(Plant plant);
    
    @Update
    void update(Plant plant);
    
    @Query("UPDATE plants SET is_bookmarked = :isBookmarked WHERE id = :plantId")
    void updateBookmarkStatus(int plantId, boolean isBookmarked);
    
    @Delete
    void delete(Plant plant);
}
```

#### GardeningTechniqueDao
- Provides database operations for gardening guides
- Handles guide filtering and sorting
- Manages guide categories

#### ForumPostDao
- Provides database operations for forum posts
- Handles post creation and interaction
- Manages post filtering and sorting

### Utilities

#### LocationHelper
- Manages location permissions and updates
- Provides current location information
- Handles geocoding for location-based features

```java
public class LocationHelper {
    private static final int LOCATION_UPDATE_INTERVAL = 30000; // 30 seconds
    private static final int FASTEST_INTERVAL = 5000; // 5 seconds
    
    private final Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;
    
    public LocationHelper(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        createLocationRequest();
        createLocationCallback();
    }
    
    private void createLocationRequest() {
        locationRequest = LocationRequest.create()
                .setInterval(LOCATION_UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }
    
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                
                for (Location location : locationResult.getLocations()) {
                    lastLocation = location;
                    // Notify listeners of location update
                }
            }
        };
    }
    
    public void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper());
        }
    }
    
    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    
    public Location getLastLocation() {
        return lastLocation;
    }
}
```

#### WeatherHelper
- Fetches weather data from API
- Parses weather information
- Provides weather forecast

#### PlantingScheduleHelper
- Manages planting schedules and reminders
- Calculates optimal planting times
- Generates task reminders

#### ImageUtils
- Handles image loading and caching
- Provides image transformation utilities
- Manages image uploads

#### ARUtils
- Interfaces with AR Core
- Manages 3D models for visualization
- Handles AR placement and rendering

### Database

#### DatabaseHelper
- Manages database creation and version management
- Handles database migrations
- Provides access to DAOs

#### AppDatabase
- Defines database schema and version
- Creates database tables
- Manages relations between entities

```java
@Database(entities = {Plant.class, User.class, GardeningTechnique.class, ForumPost.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "agrigrow_db";
    private static volatile AppDatabase instance;
    
    public abstract PlantDao plantDao();
    public abstract UserDao userDao();
    public abstract GardeningTechniqueDao gardeningTechniqueDao();
    public abstract ForumPostDao forumPostDao();
    
    public static synchronized AppDatabase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    AgriGrowApplication.getAppContext(),
                    AppDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
```

### External Services

#### WeatherAPI
- Integrates with weather service providers
- Fetches local weather conditions
- Provides forecast information

#### ARCore
- Provides augmented reality capabilities
- Handles 3D model rendering
- Manages spatial mapping

#### Maps API
- Provides location-based services
- Assists with climate zone determination
- Helps find local gardening resources

#### Image Recognition API
- Identifies plants from images
- Diagnoses plant diseases
- Analyzes plant health

### Adapters

#### PlantAdapter
- Binds plant data to RecyclerView
- Handles item click events
- Manages bookmarking interactions

```java
public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
    private List<Plant> plants = new ArrayList<>();
    private final OnPlantClickListener listener;
    
    public interface OnPlantClickListener {
        void onPlantClick(Plant plant);
        void onBookmarkClick(Plant plant, boolean isBookmarked);
    }
    
    public PlantAdapter(OnPlantClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlantBinding binding = ItemPlantBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PlantViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.bind(plant, listener);
    }
    
    @Override
    public int getItemCount() {
        return plants.size();
    }
    
    public void setPlants(List<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }
    
    static class PlantViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlantBinding binding;
        
        public PlantViewHolder(ItemPlantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        
        public void bind(Plant plant, OnPlantClickListener listener) {
            binding.textViewPlantName.setText(plant.getName());
            binding.textViewPlantDescription.setText(plant.getDescription());
            binding.textViewDifficultyLevel.setText(getDifficultyText(plant.getDifficultyLevel()));
            binding.textViewSunlight.setText(plant.getSunlightRequirement());
            binding.textViewWater.setText(getWateringText(plant.getWateringFrequency()));
            
            // Load image with Glide
            Glide.with(binding.getRoot())
                    .load(plant.getImageUrl())
                    .placeholder(R.drawable.placeholder_plant)
                    .into(binding.imageViewPlant);
            
            // Set bookmark icon based on status
            binding.imageViewBookmark.setImageResource(
                    plant.isBookmarked() ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_outline);
            
            // Set click listeners
            binding.getRoot().setOnClickListener(v -> listener.onPlantClick(plant));
            
            binding.imageViewBookmark.setOnClickListener(v -> {
                boolean newStatus = !plant.isBookmarked();
                plant.setBookmarked(newStatus);
                binding.imageViewBookmark.setImageResource(
                        newStatus ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_outline);
                listener.onBookmarkClick(plant, newStatus);
            });
        }
        
        private String getDifficultyText(int level) {
            switch (level) {
                case 1: return "Beginner";
                case 2: return "Easy";
                case 3: return "Moderate";
                case 4: return "Advanced";
                case 5: return "Expert";
                default: return "Unknown";
            }
        }
        
        private String getWateringText(int frequency) {
            if (frequency <= 1) {
                return "Daily watering";
            } else if (frequency <= 3) {
                return "Water every 2-3 days";
            } else if (frequency <= 7) {
                return "Weekly watering";
            } else {
                return "Water every " + frequency + " days";
            }
        }
    }
}
```

#### GardeningGuideAdapter
- Binds guide data to RecyclerView
- Handles item click events
- Manages guide display formats

#### ForumPostAdapter
- Binds forum post data to RecyclerView
- Handles post interactions
- Manages comments and likes