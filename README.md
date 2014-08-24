idenTREEfy-KNN
==============

Reconocimiento de especie de árboles en base a una foto de una hoja.

Pasos:
Copiar el dataset de hojas en "resources/dataset"
https://drive.google.com/folderview?id=0B7H52Vp6TgCNN3R3cTlSaVVWTzQ&usp=sharing

En Test.java si la línea que dice:
KNN.generarDataset();
No está comentada, el script recorre todo el dataset y procesa las imagenes, calcula todas las características y las guarda en:
"resources/procesadas" (imagenes procesadas)
"resources/hojas-dataset.txt" (caracteristicas de cada hoja en un csv que se usa para nutrir al KNN)
"resources/analizada.jpg" es la imagen de "test.jpg" procesada.
"resources/threshold.jpg" es la imagen de "test.jpg" luego de aplicarle los filtros.
Si está comentada, utiliza el archivo "hojas-dataset.txt" generado en una corrida anterior o el del repositorio.


