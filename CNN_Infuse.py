
"""
Created on Sun Mar 17 18:37:17 2019

@author: zenunk
"""


import warnings
warnings.filterwarnings("ignore")

from sklearn.model_selection import train_test_split, KFold, GridSearchCV, StratifiedShuffleSplit,StratifiedKFold
from sklearn.metrics import confusion_matrix, classification_report, accuracy_score
from sklearn.preprocessing import LabelEncoder
import numpy as np
import pandas as pd
from keras import layers
from keras.models import Model, Sequential
from keras.layers import Dense, Dropout, Activation, LSTM
from keras.layers import Input, Flatten
from keras.wrappers.scikit_learn import KerasClassifier
from keras.utils import np_utils, to_categorical
from keras.layers import Conv1D, MaxPooling1D, Embedding

from sklearn.utils import shuffle



#%%
#==============================================================================
# Load the dataset
#==============================================================================
# read document topics feature vectors


full_data = np.loadtxt('INFUSE_Baseline.csv',delimiter=',',skiprows=1)

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


# convert integers to dummy variables (i.e. one hot encoded)
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
Baseline = 100
Top1 = 161
Top2 = 214
Top3 = 249
Top4 = 283
Top5 = 323
'''

NoOfAtt=100



x_train, x_test, y_train, y_test = train_test_split(x, dummy_y, train_size=0.7, random_state=seed)

x_train, x_val, y_train, y_val = train_test_split(x_train, y_train, train_size=0.7, random_state=seed)


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

