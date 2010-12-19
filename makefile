main:
	/home/a/VCS/android/android-ndk-r5/ndk-build NDK_DEBUG=1
	ant install

debug:
	/home/a/VCS/android/android-ndk-r5/ndk-gdb --adb=/home/a/VCS/android/android-sdk-linux_86/platform-tools/adb --launch=stocktake
