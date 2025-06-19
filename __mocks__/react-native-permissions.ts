export const check = jest.fn(() => Promise.resolve('granted'));
export const request = jest.fn(() => Promise.resolve('granted'));
export const PERMISSIONS = {IOS: {CAMERA: 'camera', MICROPHONE: 'microphone'}, ANDROID: {CAMERA: 'camera', RECORD_AUDIO: 'microphone'}};
export const RESULTS = {GRANTED: 'granted'};
