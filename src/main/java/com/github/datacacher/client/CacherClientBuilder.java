package com.github.datacacher.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.datacacher.constants.ListConstants;
import com.github.datacacher.constants.MapConstants;
import com.github.datacacher.model.*;
import org.apache.camel.component.http.HttpDeleteWithBodyMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.datacacher.client.CacherConstants.*;
import static com.github.datacacher.constants.ListConstants.SORT;
import static com.github.datacacher.constants.ListConstants.VALUES;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class CacherClientBuilder {
    private CacherClientBuilder() {
    }

    private String host;
    private int port;

    public static CacherClientBuilder builder() {
        return new CacherClientBuilder();
    }

    public CacherClientBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public CacherClientBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public CacherClient build() {
        return new CacherClient(host, port);
    }

    public static class CacherClient {
        private String host;
        private int port;
        private String hostPort;
        private String zkConnection;
        private ObjectMapper mapper = new ObjectMapper();
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            @Override
            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };

        public CacherClient(String host, int port) {
            this.host = host;
            this.port = port;
            this.hostPort = "http://" + host + ":" + port;
        }

        public CacherClient(String zkConnection) {
            this.zkConnection = zkConnection;
        }

        public CacheResponse createCache(String cacheName, long expiry) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpPost createCacheHttp = new HttpPost();
                createCacheHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + CacherConstants.CACHE));
                createCacheHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                createCacheHttp.setHeader(ACCEPT, APPLICATION_JSON);
                CacheRequest cacheRequest = new CacheRequest();
                cacheRequest.setCacheName(cacheName);
                cacheRequest.setExpiry(expiry);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(cacheRequest));
                createCacheHttp.setEntity(entity);
                String response = httpclient.execute(createCacheHttp, responseHandler);
                return mapper.readValue(response, CacheResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public CacheResponse getCache(String cacheName) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpGet getCacheHttp = new HttpGet();
                getCacheHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + CacherConstants.CACHE
                        + FORWARDSLASH + cacheName));
                getCacheHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                getCacheHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(getCacheHttp, responseHandler);
                return mapper.readValue(response, CacheResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public CacheAuditResponse getCacheAudit(String cacheName) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpGet getAuditCacheHttp = new HttpGet();
                getAuditCacheHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + CacherConstants.CACHE
                        + FORWARDSLASH + cacheName + AUDIT));
                getAuditCacheHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                getAuditCacheHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(getAuditCacheHttp, responseHandler);
                return mapper.readValue(response, CacheAuditResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public CacheResponse destroyCache(String cacheName) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpDelete destroyCacheHttp = new HttpDelete();
                destroyCacheHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + CacherConstants.CACHE
                        + FORWARDSLASH + cacheName));
                destroyCacheHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                destroyCacheHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(destroyCacheHttp, responseHandler);
                return mapper.readValue(response, CacheResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public CacheResponse getCacheAvailability(String cacheName) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpGet getCacheAvailabilityHttp = new HttpGet();
                getCacheAvailabilityHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + CacherConstants.CACHE
                        + FORWARDSLASH + cacheName + FORWARDSLASH + CacherConstants.AVAILABLE));
                getCacheAvailabilityHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                getCacheAvailabilityHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(getCacheAvailabilityHttp, responseHandler);
                return mapper.readValue(response, CacheResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public ListResponse createList(String cacheName, String listName, List payload) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpPost createListHttp = new HttpPost();
                createListHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + FORWARDSLASH + ListConstants.LIST));
                createListHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                createListHttp.setHeader(ACCEPT, APPLICATION_JSON);
                ListRequest listRequest = new ListRequest();
                listRequest.setCacheName(cacheName);
                listRequest.setListName(listName);
                listRequest.setPayload(payload);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(listRequest));
                createListHttp.setEntity(entity);
                String response = httpclient.execute(createListHttp, responseHandler);
                return mapper.readValue(response, ListResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public ListResponse getList(String cacheName, String listName) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpGet getListHttp = new HttpGet();
                getListHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + FORWARDSLASH + ListConstants.LIST
                        + FORWARDSLASH + cacheName + FORWARDSLASH + listName));
                getListHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                getListHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(getListHttp, responseHandler);
                return mapper.readValue(response, ListResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public ListResponse updateList(String cacheName, String listName, List payload) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpPut updateListHttp = new HttpPut();
                updateListHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + FORWARDSLASH + ListConstants.LIST));
                updateListHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                updateListHttp.setHeader(ACCEPT, APPLICATION_JSON);
                ListRequest listRequest = new ListRequest();
                listRequest.setCacheName(cacheName);
                listRequest.setListName(listName);
                listRequest.setPayload(payload);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(listRequest));
                updateListHttp.setEntity(entity);
                String response = httpclient.execute(updateListHttp, responseHandler);
                return mapper.readValue(response, ListResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public ListResponse addToList(String cacheName, String listName, List payload) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpPut updateListHttp = new HttpPut();
                updateListHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + FORWARDSLASH + ListConstants.LIST
                        + FORWARDSLASH + ListConstants.ADD));
                updateListHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                updateListHttp.setHeader(ACCEPT, APPLICATION_JSON);
                ListRequest listRequest = new ListRequest();
                listRequest.setCacheName(cacheName);
                listRequest.setListName(listName);
                listRequest.setPayload(payload);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(listRequest));
                updateListHttp.setEntity(entity);
                String response = httpclient.execute(updateListHttp, responseHandler);
                return mapper.readValue(response, ListResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public ListResponse destroyList(String cacheName, String listName) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                ListRequest listRequest = new ListRequest();
                listRequest.setCacheName(cacheName);
                listRequest.setListName(listName);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(listRequest));
                HttpDeleteWithBodyMethod deleteListHttp = new HttpDeleteWithBodyMethod(
                        hostPort + CacherConstants.BASEURL + FORWARDSLASH + ListConstants.LIST,
                        entity);
                deleteListHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                deleteListHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(deleteListHttp, responseHandler);
                return mapper.readValue(response, ListResponse.class);
            } catch (IOException e) {

            }
            return null;
        }

        public ListResponse removeListValues(String cacheName, String listName, List payload) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                ListRequest listRequest = new ListRequest();
                listRequest.setCacheName(cacheName);
                listRequest.setListName(listName);
                listRequest.setPayload(payload);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(listRequest));
                HttpDeleteWithBodyMethod deleteListHttp = new HttpDeleteWithBodyMethod(
                        hostPort + CacherConstants.BASEURL + FORWARDSLASH + ListConstants.LIST
                        + VALUES,
                        entity);
                deleteListHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                deleteListHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(deleteListHttp, responseHandler);
                return mapper.readValue(response, ListResponse.class);
            } catch (IOException e) {

            }
            return null;
        }

        public ListResponse sortAndGetList(String cacheName, String listName) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpGet sortAndGetListHttp = new HttpGet(
                        hostPort + CacherConstants.BASEURL + FORWARDSLASH + ListConstants.LIST
                                + FORWARDSLASH + cacheName + FORWARDSLASH + listName + SORT);
                sortAndGetListHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                sortAndGetListHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(sortAndGetListHttp, responseHandler);
                return mapper.readValue(response, ListResponse.class);
            } catch (IOException e) {

            }
            return null;
        }

        public MapResponse createMap(String cacheName, String mapName, String key, Object value) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpPost createMapHttp = new HttpPost();
                createMapHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + FORWARDSLASH + MapConstants.MAP));
                createMapHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                createMapHttp.setHeader(ACCEPT, APPLICATION_JSON);
                MapRequest mapRequest = new MapRequest();
                mapRequest.setCacheName(cacheName);
                mapRequest.setMapName(mapName);
                mapRequest.setKeyName(key);
                mapRequest.setValues(value);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(mapRequest));
                createMapHttp.setEntity(entity);
                String response = httpclient.execute(createMapHttp, responseHandler);
                return mapper.readValue(response, MapResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }
        public MapResponse getMap(String cacheName, String mapName) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpGet getMapHttp = new HttpGet();
                getMapHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + FORWARDSLASH + MapConstants.MAP
                        + FORWARDSLASH + cacheName + FORWARDSLASH + mapName));
                getMapHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                getMapHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(getMapHttp, responseHandler);
                return mapper.readValue(response, MapResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }
        public MapResponse putMap(String cacheName, String mapName, String key, Object value) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpPut putMapHttp = new HttpPut();
                putMapHttp.setURI(new URI(hostPort + CacherConstants.BASEURL + FORWARDSLASH + MapConstants.MAP));
                putMapHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                putMapHttp.setHeader(ACCEPT, APPLICATION_JSON);
                MapRequest mapRequest = new MapRequest();
                mapRequest.setCacheName(cacheName);
                mapRequest.setMapName(mapName);
                mapRequest.setKeyName(key);
                mapRequest.setValues(value);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(mapRequest));
                putMapHttp.setEntity(entity);
                String response = httpclient.execute(putMapHttp, responseHandler);
                return mapper.readValue(response, MapResponse.class);
            } catch (IOException e) {

            } catch (URISyntaxException e) {

            }
            return null;
        }

        public MapResponse destroyMap(String cacheName, String mapName) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                MapRequest mapRequest = new MapRequest();
                mapRequest.setCacheName(cacheName);
                mapRequest.setMapName(mapName);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(mapRequest));
                HttpDeleteWithBodyMethod destroyMapHttp = new HttpDeleteWithBodyMethod(
                        hostPort + CacherConstants.BASEURL + FORWARDSLASH + MapConstants.MAP,
                        entity);
                destroyMapHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                destroyMapHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(destroyMapHttp, responseHandler);
                return mapper.readValue(response, MapResponse.class);
            } catch (IOException e) {

            }
            return null;
        }

        public MapResponse destroyMapKey(String cacheName, String mapName,  String key) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                MapRequest mapRequest = new MapRequest();
                mapRequest.setCacheName(cacheName);
                mapRequest.setMapName(mapName);
                mapRequest.setKeyName(key);
                HttpEntity entity = new StringEntity(mapper.writeValueAsString(mapRequest));
                HttpDeleteWithBodyMethod destroyMapHttp = new HttpDeleteWithBodyMethod(
                        hostPort + CacherConstants.BASEURL + FORWARDSLASH + MapConstants.MAP
                                + FORWARDSLASH + MapConstants.KEY,
                        entity);
                destroyMapHttp.setHeader(CONTENTTYPE, APPLICATION_JSON);
                destroyMapHttp.setHeader(ACCEPT, APPLICATION_JSON);
                String response = httpclient.execute(destroyMapHttp, responseHandler);
                return mapper.readValue(response, MapResponse.class);
            } catch (IOException e) {

            }
            return null;
        }
    }
}
