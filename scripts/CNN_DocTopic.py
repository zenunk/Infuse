"""
Created on Mon Mar  4 17:55:11 2019
@author: zekaaa

"""

import warnings
warnings.filterwarnings("ignore")
import numpy as np
from keras.models import Sequential
from keras.layers import Dense, Dropout
from keras.wrappers.scikit_learn import KerasClassifier
from keras.utils import np_utils, to_categorical
from keras.optimizers import Adam ,RMSprop
from sklearn.model_selection import train_test_split, KFold, GridSearchCV, StratifiedShuffleSplit,StratifiedKFold
from sklearn.metrics import confusion_matrix, classification_report, accuracy_score
from sklearn.preprocessing import LabelEncoder
from sklearn.pipeline import Pipeline
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
import pandas as pd
from keras import layers
from keras.models import Model, Sequential
from keras.layers import Dense, Dropout, Activation, LSTM
from keras.layers import Input, Flatten, merge, Lambda, Dropout
from keras.wrappers.scikit_learn import KerasClassifier
from keras.layers.wrappers import TimeDistributed, Bidirectional
from keras.utils import np_utils, to_categorical
from keras.optimizers import Adam, RMSprop
from keras.layers import Conv1D, MaxPooling1D, Embedding
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences
from keras.layers import Conv1D, MaxPooling1D, Embedding
from keras.layers.normalization import BatchNormalization
import gensim
from gensim.models import Word2Vec
from gensim.utils import simple_preprocess
from gensim.models.keyedvectors import KeyedVectors
import matplotlib.pyplot as plt
import itertools
from nltk.stem.wordnet import WordNetLemmatizer
from nltk.tokenize import RegexpTokenizer
from sklearn.utils import shuffle
from xlrd import open_workbook

#%%
#==============================================================================
# Load the dataset
#==============================================================================
# read document topics feature vectors

data = pd.read_csv('DT300d.csv', header=None)

df = pd.DataFrame(data).fillna(0)
topics = df.values
x = topics[1:, 1:301].astype(float)

book = open_workbook("Infuse_Dataset_2019.xlsx","utf-8")
sheet = book.sheet_by_index(0) #If your data is on sheet 1
y = []
lastrow=468


for row in range(1, lastrow): #start from 1, to leave out row 0
    y.append(sheet.cell_value(row, 1)) #extract from first col

#%%
#==============================================================================
# Encode class values as integers 
#==============================================================================
encoder = LabelEncoder()
encoder.fit(y)
encoded_y = encoder.transform(y)
dummy_y = np_utils.to_categorical(encoded_y)




#==============================================================================
# Define full_multiclass_report which prints classification report
#==============================================================================    
## If binary (sigmoid output), set binary parameter to True
def full_multiclass_report(model,
                           x,
                           y_true,
                           classes,
                           batch_size=64,
                           binary=False):

    # 1. Transform one-hot encoded y_true into their class number
    if not binary:
        y_true = np.argmax(y_true,axis=1)
    
    # 2. Predict classes and stores in y_pred
    y_pred = model.predict_classes(x, batch_size=batch_size)
    
    # 3. Print accuracy score
    print("Accuracy : "+ str(accuracy_score(y_true,y_pred)))
    
    print("")
    
    # 4. Print classification report
    print("Classification Report")
    print(classification_report(y_true,y_pred,digits=4))    
    
  
  


#==============================================================================
# Training, testting and validation split
#==============================================================================
seed =1000

'''
Neurons = 1024
NoEnrichment = 100
Top1 = 161
Top2 = 214
Top3 = 249
Top4 = 283
Top5 = 323
'''

NoOfAtt=165

x_train, x_test, y_train, y_test = train_test_split(x, dummy_y, train_size=0.7, random_state=seed)
x_train, x_val, y_train, y_val = train_test_split(x_train, y_train, train_size=0.7, random_state=seed)

print(x_train.shape)

x_train = x_train.reshape(x_train.shape[0],NoOfAtt,1)
x_test = x_test.reshape(x_test.shape[0],NoOfAtt,1)
x_val = x_val.reshape(x_val.shape[0],NoOfAtt,1)

#==============================================================================
# Build CNN model
#==============================================================================
CNN_Model = Sequential()
CNN_Model.add(layers.Conv1D(1024, 2, input_shape=(NoOfAtt,1), activation='relu'))
CNN_Model.add(layers.Conv1D(1024, 2, activation='relu'))
CNN_Model.add(layers.Conv1D(1024, 2, activation='relu'))
CNN_Model.add(Flatten())
CNN_Model.add(layers.Dense(5, activation='softmax'))

CNN_Model.compile(loss='categorical_crossentropy',optimizer='adam', metrics=['accuracy'])
CNN_Model.summary()


#==============================================================================
# Evaluate model and print results
#==============================================================================

CNN_History=CNN_Model.fit(x_train, y_train, epochs = 75, batch_size = 1048,verbose=2, validation_data=(x_val,y_val), shuffle=True)
full_multiclass_report(CNN_Model, x_val, y_val, encoder.inverse_transform(np.arange(5)))

