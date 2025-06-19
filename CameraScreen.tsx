import React, {useEffect, useRef, useState} from 'react';
import {Platform, StyleSheet, View} from 'react-native';
import {Camera, useCameraDevices, useCameraPermission} from 'react-native-vision-camera';
import {check, request, PERMISSIONS, RESULTS} from 'react-native-permissions';
import {Button, IconButton, Provider as PaperProvider} from 'react-native-paper';
import Slider from '@react-native-community/slider';

export default function CameraScreen() {
  const cameraRef = useRef<Camera>(null);
  const {hasPermission, requestPermission} = useCameraPermission();
  const devices = useCameraDevices();
  const device = devices.back;

  const [iso, setIso] = useState(0);
  const [shutter, setShutter] = useState(0);
  const [whiteBalance, setWhiteBalance] = useState<'auto' | 'sunny' | 'cloudy' | 'shadow' | 'fluorescent' | 'incandescent'>('auto');
  const [flash, setFlash] = useState(false);

  useEffect(() => {
    (async () => {
      if (!hasPermission) {
        await requestPermission();
      }
      const camPerm =
        Platform.OS === 'ios' ? PERMISSIONS.IOS.CAMERA : PERMISSIONS.ANDROID.CAMERA;
      const micPerm =
        Platform.OS === 'ios' ? PERMISSIONS.IOS.MICROPHONE : PERMISSIONS.ANDROID.RECORD_AUDIO;
      const status = await check(camPerm);
      if (status !== RESULTS.GRANTED) {
        await request(camPerm);
      }
      const micStatus = await check(micPerm);
      if (micStatus !== RESULTS.GRANTED) {
        await request(micPerm);
      }
    })();
  }, [hasPermission, requestPermission]);

  if (device == null) return <View style={styles.container} />;

  return (
    <PaperProvider>
      <View style={styles.container}>
        <Camera
          ref={cameraRef}
          style={StyleSheet.absoluteFill}
          device={device}
          isActive={true}
          photo={true}
          // @ts-ignore vision-camera supports iso prop
          iso={iso}
          // @ts-ignore vision-camera supports exposureDuration prop
          exposureDuration={shutter}
          // @ts-ignore vision-camera supports whiteBalance prop
          whiteBalance={whiteBalance}
          torch={flash ? 'on' : 'off'}
        />
        <View style={styles.controls}>
          <View style={styles.row}>
            <IconButton
              icon={flash ? 'flash' : 'flash-off'}
              size={24}
              onPress={() => setFlash(f => !f)}
            />
            <Button mode="contained" onPress={() => setWhiteBalance('auto')}>
              WB Auto
            </Button>
          </View>
          <View style={styles.sliderRow}>
            <Slider
              style={styles.slider}
              minimumValue={device?.minISO ?? 0}
              maximumValue={device?.maxISO ?? 1}
              value={iso}
              onValueChange={setIso}
            />
          </View>
          <View style={styles.sliderRow}>
            <Slider
              style={styles.slider}
              minimumValue={0}
              maximumValue={1}
              value={shutter}
              onValueChange={setShutter}
            />
          </View>
        </View>
      </View>
    </PaperProvider>
  );
}

const styles = StyleSheet.create({
  container: {flex: 1, backgroundColor: 'black'},
  controls: {position: 'absolute', bottom: 20, width: '100%'},
  row: {flexDirection: 'row', justifyContent: 'space-evenly', marginBottom: 10},
  sliderRow: {paddingHorizontal: 20, marginVertical: 5},
  slider: {flex: 1},
});
