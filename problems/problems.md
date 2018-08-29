Current problems:

Having Customer.userId to be primitive will produce unnecessary allocations in lookups etc.
Memory leak in DataAccess.removeCustomer.