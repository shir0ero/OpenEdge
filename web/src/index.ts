
const img = document.getElementById("frame") as HTMLImageElement;
img.src = "frame.png"; // For demo, use a static PNG exported from Android

const fpsText = document.getElementById("fps") as HTMLSpanElement;
fpsText.textContent = "Resolution: 640x480 | FPS: 15";
