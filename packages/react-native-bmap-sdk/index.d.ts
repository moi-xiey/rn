declare module '@moi-xiey/react-native-bmap-sdk' {
  import React from 'react';
  import {EmitterSubscription, ViewProps} from 'react-native';

  type Options = {
    gps: boolean;
    distanceFilter: number;
  };

  type TLocation = {
    accuracy?: number,
    latitude: number,
    longitude: number,
    direction: number,
    locationType: number,
  };

  type Listener = (
    listener: {
      timestamp: number;
      altitude: number;
      speed: number;
    } & TLocation,
  ) => void;

  type LatLng = {
    latitude: number,
    longitude: number,
  }

  type Region = {
    latitudeDelta: number,
    longitudeDelta: number,
  } & LatLng;

  type SearchResult = { address: string } & LatLng;

  type ReverseResult = {
    country: string;
    countryCode: string;
    province: string;
    city: string;
    cityCode: string;
    district: string;
    street: string;
    streetNumber: string;
    businessCircle: string;
    adCode: string;
    address: string;
    description: string;
  } & LatLng;

  type MapStatus = {
    center: LatLng,
    region: Region,
    overlook: number,
    rotation: number,
    zoomLevel: number,
  };

  type MapViewProps = {
    satellite?: boolean;
    trafficEnabled?: boolean;
    baiduHeatMapEnabled?: boolean;
    indoorEnabled?: boolean;
    buildingsDisabled?: boolean;
    minZoomLevel?: number;
    maxZoomLevel?: number;
    compassDisabled?: boolean;
    zoomControlsDisabled?: boolean;
    scaleBarDisabled?: boolean;
    scrollDisabled?: boolean;
    overlookDisabled?: boolean;
    rotateDisabled?: boolean;
    zoomDisalbed?: boolean;
    center?: LatLng;
    zoomLevel?: number;
    rotation?: number;
    overlook?: number;
    paused?: boolean;
    locationEnabled?: boolean;
    location?: TLocation;
    locationMode?: 'normal' | 'follow' | 'compass';
    campassMode?: true;
    onLoad?: () => void;
    onClick?: (coordinate: LatLng) => void;
    onLongClick?: (coordinate: LatLng) => void;
    onDoubleClick?: (coordinate: LatLng) => void;
    onStatusChange?: (mapStatus: MapStatus) => void;
  } & ViewProps;

  export class Initializer {
    static init(key: string): Promise<void>;
  }

  export class Location {
    static init(): Promise<void>;

    static start(): void;

    static stop(): void;

    static setOptions(options: Options): void;

    static addLocationListener(listener: Listener): EmitterSubscription;
  }

  export class Geocode {
    static search(address: string, city?: string): Promise<SearchResult>;

    static reverse(coordinate: LatLng): Promise<ReverseResult>;
  }

  export class MapView extends React.Component<MapViewProps> {}
}
