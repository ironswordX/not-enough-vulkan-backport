# Not Enough Vulkan Backport
> This mod is an unofficial backport of the mod Not Enough Vulkan, modified to work on versions lower than 1.21.11 (the only version that Not Enough Vulkan currently supports)

![Logo](/src/client/resources/assets/not-enough-vulkan/textures/icon.png)

---

# ⚠️ DISCLAIMER ⚠️

This mod is not guaranteed to be stable, as it's a very hacky solution to get the mod to run on versions older than 1.21.11. This is due to the fact that [Sodium Extra](https://github.com/FlashyReese/sodium-extra/) had a large refactor between 1.21.10 and 1.21.11. 

## Building from source
### Prerequisites
- Java Development Kit 17 or above
- Git
### Compiling
1. Clone the repository and navigate into the cloned repository.
   ```
   git clone https://github.com/ironswordX/not-enough-vulkan-backport.git
   cd not-enough-vulkan-backport
   ```
2. Navigate to the directory you've cloned this repository and launch a build with Gradle. If you are not using the Gradle wrapper, simply replace `gradlew` with `gradle` or the path to it.
   - Windows
   ```
   gradlew build
   ```
   - Linux/macOS
   ```
   ./gradlew build
   ```
3. The initial setup may take a few minutes. After Gradle has finished building everything, you can find the resulting artifacts in `build/libs`.

## License
Not Enough Vulkan Backport is licensed under GNU LGPLv3, a free and open-source license. For more information, please see the [license file](LICENSE.txt).
