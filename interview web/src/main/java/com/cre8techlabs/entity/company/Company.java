package com.cre8techlabs.entity.company;

import com.cre8techlabs.entity.EntityMod;
import com.cre8techlabs.entity.component.Address;
import com.cre8techlabs.entity.component.ContactDetails;
import com.cre8techlabs.entity.component.ContactPerson;

public class Company extends EntityMod {
	public Company() {
    }
	protected boolean company = true;
	protected String name;
	
	protected String shortName;
	
	protected String website;
	protected String ein;
	
	protected boolean active;
	
	protected ContactPerson contactPerson = new ContactPerson();
	
	
	protected Address address = new Address();
	
	
	protected ContactDetails contactDetails = new ContactDetails();
	protected Address mailingAddress = new Address();
	
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEin() {
		return ein;
	}
	public void setEin(String ein) {
		this.ein = ein;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public ContactPerson getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(ContactPerson contactPerson) {
		this.contactPerson = contactPerson;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public ContactDetails getContactDetails() {
		return contactDetails;
	}
	public void setContactDetails(ContactDetails contactDetails) {
		this.contactDetails = contactDetails;
	}
	public Address getMailingAddress() {
		return mailingAddress;
	}
	public void setMailingAddress(Address mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getShortName() {
		return shortName == null? name: shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public String toString() {
		return "Company [name=" + name + ", shortName=" + shortName + ", active=" + active + ", adress= " + address.getCity() +"]";
	}
	public boolean isCompany() {
		return company;
	}
	public void setCompany(boolean company) {
		this.company = company;
	}
}
