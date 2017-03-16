class TooManyUsers extends Exception {
		String strError;

		TooManyUsers(String strError) {
			this.strError = strError;
		}

		public String toString() {
			return ("Error From Server: " + strError);
		}
	}