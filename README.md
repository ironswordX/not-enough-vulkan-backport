# Not Enough Vulkan Backport
> This mod is an official fork of an unofficial backport of my mod Not Enough Vulkan, modified to work on 1.21.10, and VulkanMod 0.6.3+

![Logo](/src/client/resources/assets/not-enough-vulkan/textures/icon.png)

---

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
