"""
Created on Mon Mar  4 17:55:11 2019
@author: zekaaa

"""

import warnings
warnings.filterwarnings("ignore")


import string
import numpy as np
from keras.models import Sequential
from keras.layers import Dense, Dropout
from keras.wrappers.scikit_learn import KerasClassifier
from keras.utils import np_utils, to_categorical
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
from gensim.models.keyedvectors import KeyedVectors
import matplotlib.pyplot as plt
import itertools
from nltk.stem.wordnet import WordNetLemmatizer
from nltk.tokenize import RegexpTokenizer
from sklearn.utils import shuffle



#%%
#==============================================================================
# Load the dataset
#==============================================================================
# read document topics feature vectors

import numpy as np

full_data = np.loadtxt('INFUSE_Top3.csv',delimiter=',',skiprows=1)

x = full_data[:,0:(len(full_data[1])-1)]
y = full_data[:,len(full_data[1])-1]

print(len(full_data[1])-1)

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


Neurons = 1024
Baseline = 100
Top1 = 161
Top2 = 214
Top3 = 249
Top4 = 283
Top5 = 323


NoOfAtt=Top3



x_train, x_test, y_train, y_test = train_test_split(x, dummy_y, train_size=0.7, random_state=seed)

x_train, x_val, y_train, y_val = train_test_split(x_train, y_train, train_size=0.7, random_state=seed)

print(x_train.shape)


x_train = x_train.reshape(x_train.shape[0],NoOfAtt,1)
x_test = x_test.reshape(x_test.shape[0],NoOfAtt,1)
x_val = x_val.reshape(x_val.shape[0],NoOfAtt,1)


#==============================================================================
# Build CNN model
#==============================================================================

LSTM_Model = Sequential()
LSTM_Model.add(LSTM(units = 1024, return_sequences = True, input_shape = (x_train.shape[1], 1)))
LSTM_Model.add(LSTM(units = 1024, return_sequences = True, input_shape = (x_train.shape[1], 1)))
LSTM_Model.add(LSTM(units = 1024, return_sequences = True, input_shape = (x_train.shape[1], 1)))
LSTM_Model.add(Flatten())
LSTM_Model.add(layers.Dense(5, activation='softmax'))
LSTM_Model.compile(loss='categorical_crossentropy',optimizer='adam', metrics=['accuracy'])

LSTM_Model.summary()


#==============================================================================
# Evaluate model and print results
#==============================================================================

LSTM_History=LSTM_Model.fit(x_train, y_train, epochs = 75, batch_size = 64,verbose=2, validation_data=(x_val,y_val), shuffle=False)

full_multiclass_report(LSTM_Model, x_val, y_val, encoder.inverse_transform(np.arange(5)))

