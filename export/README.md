# AgriGrow - Urban Gardening Assistant

A comprehensive Android mobile application tailored for urban gardeners, combining advanced technology with community-driven plant care solutions.

## Project Setup

### Prerequisites
- Android Studio (Latest version recommended)
- JDK 17 or higher
- Android SDK (API Level 32)

### API Keys Required
This project uses the following APIs that require authentication:

1. **Plant.id API** - Used for plant identification from images
   - Sign up at [Plant.id](https://plant.id/) to get your API key

2. **OpenWeatherMap API** - Used for weather data integration
   - Sign up at [OpenWeatherMap](https://openweathermap.org/api) to get your API key

### Setting Up API Keys
1. In your project root directory, create or edit the `local.properties` file
2. Add the following lines, replacing the placeholders with your actual API keys:
   ```properties
   PLANT_ID_API_KEY=your_plant_id_api_key_here
   OPENWEATHERMAP_API_KEY=your_openweathermap_api_key_here
   ```

### Building the Project
1. Open the project in Android Studio
2. Sync the Gradle files
3. Build and run the application on an emulator or physical device

## Features

- Plant identification using camera or gallery images
- Personalized gardening plans based on local conditions
- Weather integration for optimal planting and care schedules
- Community forum for knowledge sharing
- Plant care calendar and reminders
- Comprehensive gardening guides and video tutorials
- Soil testing recommendations
- Achievement-based gamification system

## Architecture

- MVVM architecture pattern
- Room database for local storage
- Material Design UI components
- API integrations via Retrofit and OkHttp
- Background processing with WorkManager

## Contributing

If you'd like to contribute to this project, please follow these guidelines:
1. Fork the repository
2. Create a feature branch
3. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.